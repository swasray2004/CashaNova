package com.example.budgettracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.models.ExpenseCategory
import com.example.budgettracker.data.models.Transaction
import com.example.budgettracker.data.models.TransactionType
import com.example.budgettracker.data.repository.AuthRepository
import com.example.budgettracker.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class TransactionViewModel : ViewModel() {
    private val repository = TransactionRepository()
    private val authRepository = AuthRepository()

    // Get the current user ID from Firebase Auth
    private val userId: String
        get() = authRepository.currentUser?.uid ?: ""

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _incomeTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val incomeTransactions: StateFlow<List<Transaction>> = _incomeTransactions.asStateFlow()

    private val _expenseTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val expenseTransactions: StateFlow<List<Transaction>> = _expenseTransactions.asStateFlow()

    private val _currentMonthTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val currentMonthTransactions: StateFlow<List<Transaction>> = _currentMonthTransactions.asStateFlow()

    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome.asStateFlow()

    private val _totalExpense = MutableStateFlow(0.0)
    val totalExpense: StateFlow<Double> = _totalExpense.asStateFlow()

    private val _expensesByCategory = MutableStateFlow<Map<ExpenseCategory, Double>>(emptyMap())
    val expensesByCategory: StateFlow<Map<ExpenseCategory, Double>> = _expensesByCategory.asStateFlow()

    private val _selectedDateRange = MutableStateFlow<Pair<Long, Long>>(getCurrentMonthRange())
    val selectedDateRange: StateFlow<Pair<Long, Long>> = _selectedDateRange.asStateFlow()

    init {
        if (authRepository.isUserLoggedIn) {
            loadAllTransactions()
            loadIncomeTransactions()
            loadExpenseTransactions()
            loadCurrentMonthTransactions()
        }
    }

    private fun loadAllTransactions() {
        viewModelScope.launch {
            repository.getAllTransactions(userId).collect { transactionList ->
                _transactions.value = transactionList
            }
        }
    }

    private fun loadIncomeTransactions() {
        viewModelScope.launch {
            repository.getTransactionsByType(userId, TransactionType.INCOME).collect { transactionList ->
                _incomeTransactions.value = transactionList
            }
        }
    }

    private fun loadExpenseTransactions() {
        viewModelScope.launch {
            repository.getTransactionsByType(userId, TransactionType.EXPENSE).collect { transactionList ->
                _expenseTransactions.value = transactionList
            }
        }
    }

    private fun loadCurrentMonthTransactions() {
        viewModelScope.launch {
            repository.getTransactionsForCurrentMonth(userId).collect { transactionList ->
                _currentMonthTransactions.value = transactionList
                calculateTotals(transactionList)
                calculateExpensesByCategory(transactionList)
            }
        }
    }

    private fun calculateTotals(transactions: List<Transaction>) {
        val income = transactions.filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
        val expense = transactions.filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }

        _totalIncome.value = income
        _totalExpense.value = expense
    }

    private fun calculateExpensesByCategory(transactions: List<Transaction>) {
        val expenseMap = mutableMapOf<ExpenseCategory, Double>()

        transactions.filter { it.type == TransactionType.EXPENSE && it.category != null }
            .forEach { transaction ->
                transaction.category?.let { category ->
                    val currentAmount = expenseMap.getOrDefault(category, 0.0)
                    expenseMap[category] = currentAmount + transaction.amount
                }
            }

        _expensesByCategory.value = expenseMap
    }

    fun addTransaction(
        amount: Double,
        description: String,
        type: TransactionType,
        category: ExpenseCategory? = null
    ) {
        viewModelScope.launch {
            val transaction = Transaction(
                amount = amount,
                description = description,
                date = Date().time,
                type = type,
                category = category,
                userId = userId
            )
            repository.addTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
        }
    }

    fun deleteTransaction(transactionId: String) {
        viewModelScope.launch {
            repository.deleteTransaction(transactionId)
        }
    }

    fun setDateRange(startDate: Long, endDate: Long) {
        _selectedDateRange.value = Pair(startDate, endDate)
        loadTransactionsByDateRange(startDate, endDate)
    }

    private fun loadTransactionsByDateRange(startDate: Long, endDate: Long) {
        viewModelScope.launch {
            repository.getTransactionsByDateRange(userId, startDate, endDate).collect { transactionList ->
                _currentMonthTransactions.value = transactionList
                calculateTotals(transactionList)
                calculateExpensesByCategory(transactionList)
            }
        }
    }

    private fun getCurrentMonthRange(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfMonth = calendar.timeInMillis
        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.MILLISECOND, -1)
        val endOfMonth = calendar.timeInMillis

        return Pair(startOfMonth, endOfMonth)
    }
}
