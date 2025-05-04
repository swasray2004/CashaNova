package com.example.budgettracker.data.repository

import com.example.budgettracker.data.models.ExpenseCategory
import com.example.budgettracker.data.models.Transaction
import com.example.budgettracker.data.models.TransactionType
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.Date
import java.util.UUID

class TransactionRepository {
    private val database = FirebaseDatabase.getInstance()
    private val transactionsRef = database.getReference("transactions")

    suspend fun addTransaction(transaction: Transaction): String {
        val id = UUID.randomUUID().toString()
        val newTransaction = transaction.copy(id = id)
        transactionsRef.child(id).setValue(newTransaction).await()
        return id
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionsRef.child(transaction.id).setValue(transaction).await()
    }

    suspend fun deleteTransaction(transactionId: String) {
        transactionsRef.child(transactionId).removeValue().await()
    }

    fun getAllTransactions(userId: String): Flow<List<Transaction>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val transactions = snapshot.children.mapNotNull { it.getValue(Transaction::class.java) }
                    .filter { it.userId == userId }
                trySend(transactions)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        }

        transactionsRef.addValueEventListener(listener)

        awaitClose {
            transactionsRef.removeEventListener(listener)
        }
    }

    fun getTransactionsByType(userId: String, type: TransactionType): Flow<List<Transaction>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val transactions = snapshot.children.mapNotNull { it.getValue(Transaction::class.java) }
                    .filter { it.userId == userId && it.type == type }
                trySend(transactions)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        }

        transactionsRef.addValueEventListener(listener)

        awaitClose {
            transactionsRef.removeEventListener(listener)
        }
    }

    fun getTransactionsForCurrentMonth(userId: String): Flow<List<Transaction>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val calendar = Calendar.getInstance()
                calendar.time = Date()
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val startOfMonth = calendar.timeInMillis

                calendar.add(Calendar.MONTH, 1)
                val startOfNextMonth = calendar.timeInMillis

                val transactions = snapshot.children.mapNotNull { it.getValue(Transaction::class.java) }
                    .filter {
                        it.userId == userId &&
                                it.date >= startOfMonth &&
                                it.date < startOfNextMonth
                    }
                trySend(transactions)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        }

        transactionsRef.addValueEventListener(listener)

        awaitClose {
            transactionsRef.removeEventListener(listener)
        }
    }

    fun getTransactionsByDateRange(userId: String, startDate: Long, endDate: Long): Flow<List<Transaction>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val transactions = snapshot.children.mapNotNull { it.getValue(Transaction::class.java) }
                    .filter {
                        it.userId == userId &&
                                it.date >= startDate &&
                                it.date <= endDate
                    }
                trySend(transactions)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        }

        transactionsRef.addValueEventListener(listener)

        awaitClose {
            transactionsRef.removeEventListener(listener)
        }
    }
}
