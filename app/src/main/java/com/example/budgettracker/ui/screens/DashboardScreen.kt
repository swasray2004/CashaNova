package com.example.budgettracker.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.budgettracker.data.models.ExpenseCategory
import com.example.budgettracker.ui.components.ExpensePieChart
import com.example.budgettracker.ui.components.ProfileMenu
import com.example.budgettracker.ui.viewmodels.AuthViewModel
import com.example.budgettracker.ui.viewmodels.TransactionViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val totalIncome by transactionViewModel.totalIncome.collectAsState()
    val totalExpense by transactionViewModel.totalExpense.collectAsState()
    val expensesByCategory by transactionViewModel.expensesByCategory.collectAsState()
    val currentMonthTransactions by transactionViewModel.currentMonthTransactions.collectAsState()

    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
        currency = java.util.Currency.getInstance("INR")
    }
    val dateFormatter = SimpleDateFormat("MMM yyyy", Locale.getDefault())
    val currentDate = dateFormatter.format(Date())

    // Dark background with purple accents
    val darkBackground = Color(0xFF121212)
    val deepPurple = Color(0xFFBB86FC)
    val mediumPurple = Color(0xFF9C64FF)
    val darkPurple = Color(0xFF7F39FB)

    Scaffold(
        containerColor = darkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Dashboard",
                        color = deepPurple,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E1E1E),
                    titleContentColor = deepPurple
                ),
                actions = {
                    ProfileMenu(navController = navController, authViewModel = authViewModel)
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(darkBackground)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "Financial Summary",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = deepPurple
                    )
                    Text(
                        text = currentDate,
                        style = MaterialTheme.typography.bodyLarge,
                        color = mediumPurple
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SummaryCard(
                        title = "Income",
                        amount = currencyFormatter.format(totalIncome),
                        color = Color(0xFF4CAF50),  // Green
                        modifier = Modifier.weight(1f)
                    )

                    SummaryCard(
                        title = "Expenses",
                        amount = currencyFormatter.format(totalExpense),
                        color = Color(0xFFF44336),  // Red
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                SummaryCard(
                    title = "Balance",
                    amount = currencyFormatter.format(totalIncome - totalExpense),
                    color = deepPurple,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1E1E1E)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Expense Breakdown",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = deepPurple
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (expensesByCategory.isNotEmpty()) {
                            ExpensePieChart(
                                expensesByCategory = expensesByCategory,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                expensesByCategory.forEach { (category, amount) ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = category.displayName,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.White
                                        )
                                        Text(
                                            text = currencyFormatter.format(amount),
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = mediumPurple
                                        )
                                    }
                                    Divider(color = darkPurple.copy(alpha = 0.5f))
                                }
                            }
                        } else {
                            Text(
                                text = "No expenses recorded for this period",
                                style = MaterialTheme.typography.bodyLarge,
                                color = mediumPurple
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1E1E1E)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Recent Transactions",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = deepPurple
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            if (currentMonthTransactions.isNotEmpty()) {
                items(currentMonthTransactions.sortedByDescending { it.date }.take(5)) { transaction ->
                    TransactionItem(
                        description = transaction.description,
                        amount = currencyFormatter.format(transaction.amount),
                        date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(transaction.date)),
                        isExpense = transaction.category != null,
                        category = transaction.category?.displayName
                    )
                }
            } else {
                item {
                    Text(
                        text = "No transactions recorded for this period",
                        style = MaterialTheme.typography.bodyLarge,
                        color = mediumPurple,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryCard(
    title: String,
    amount: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = color.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = amount,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
fun TransactionItem(
    description: String,
    amount: String,
    date: String,
    isExpense: Boolean,
    category: String? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(0.5.dp, Color(0xFF333333))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Text(
                    text = amount,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isExpense) Color(0xFFF44336) else Color(0xFF4CAF50)  // Red or Green
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF9C64FF)
                )

                if (category != null) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF9C64FF)
                    )
                }
            }
        }
    }
}

