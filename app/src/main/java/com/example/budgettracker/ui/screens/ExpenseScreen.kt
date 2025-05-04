package com.example.budgettracker.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.budgettracker.data.models.ExpenseCategory
import com.example.budgettracker.data.models.Transaction
import com.example.budgettracker.data.models.TransactionType
import com.example.budgettracker.ui.viewmodels.TransactionViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(
    navController: NavController,
    viewModel: TransactionViewModel = viewModel()
) {
    val expenseTransactions by viewModel.expenseTransactions.collectAsState()

    var showAddExpenseDialog by remember { mutableStateOf(false) }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<ExpenseCategory?>(null) }

    // Dark theme colors
    val darkBackground = Color(0xFF121212)
    val cardColor = Color(0xFF1E1E1E)
    val deepPurple = Color(0xFFBB86FC)
    val mediumPurple = Color(0xFF9C64FF)
    val accentPurple = Color(0xFF7F39FB)
    val textColor = Color(0xFFFFFFFF)
    val secondaryTextColor = mediumPurple

    Scaffold(
        containerColor = darkBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddExpenseDialog = true },
                containerColor = accentPurple
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add Expense",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(darkBackground)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Expenses",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = deepPurple
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (showAddExpenseDialog) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = cardColor
                    ),
                    border = BorderStroke(1.dp, accentPurple.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Add New Expense",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = deepPurple
                        )

                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Amount", color = secondaryTextColor) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                focusedTextColor = textColor,
                                unfocusedTextColor = textColor,
                                unfocusedLabelColor = secondaryTextColor,
                                focusedBorderColor = accentPurple,
                                unfocusedBorderColor = mediumPurple.copy(alpha = 0.5f)
                            )
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description", color = secondaryTextColor) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                focusedTextColor = textColor,
                                unfocusedTextColor = textColor,
                                unfocusedLabelColor = secondaryTextColor,
                                focusedBorderColor = accentPurple,
                                unfocusedBorderColor = mediumPurple.copy(alpha = 0.5f)
                            )
                        )

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedCategory?.displayName ?: "Select Category",
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = expanded,

                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                    focusedTextColor = textColor,
                                    unfocusedTextColor = textColor,
                                    unfocusedLabelColor = secondaryTextColor,
                                    focusedBorderColor = accentPurple,
                                    unfocusedBorderColor = mediumPurple.copy(alpha = 0.5f)
                                ),
                                label = { Text("Category", color = secondaryTextColor) }
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.background(cardColor)
                            ) {
                                ExpenseCategory.values().forEach { category ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                category.displayName,
                                                color = textColor
                                            )
                                        },
                                        onClick = {
                                            selectedCategory = category
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = {
                                    showAddExpenseDialog = false
                                    amount = ""
                                    description = ""
                                    selectedCategory = null
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = cardColor,
                                    contentColor = deepPurple
                                ),
                                border = BorderStroke(1.dp, deepPurple)
                            ) {
                                Text("Cancel")
                            }

                            Spacer(modifier = Modifier.padding(8.dp))

                            Button(
                                onClick = {
                                    if (amount.isNotEmpty() && description.isNotEmpty() && selectedCategory != null) {
                                        viewModel.addTransaction(
                                            amount = amount.toDoubleOrNull() ?: 0.0,
                                            description = description,
                                            type = TransactionType.EXPENSE,
                                            category = selectedCategory
                                        )
                                        showAddExpenseDialog = false
                                        amount = ""
                                        description = ""
                                        selectedCategory = null
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = accentPurple,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Add")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (expenseTransactions.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No expense transactions yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = secondaryTextColor
                    )
                    Text(
                        text = "Tap the + button to add an expense",
                        style = MaterialTheme.typography.bodyMedium,
                        color = secondaryTextColor
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(expenseTransactions.sortedByDescending { it.date }) { transaction ->
                        ExpenseItem(
                            transaction = transaction,
                            onDelete = { viewModel.deleteTransaction(transaction.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(
    transaction: Transaction,
    onDelete: () -> Unit
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
        currency = java.util.Currency.getInstance("INR")
    }
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    // Dark theme colors
    val cardColor = Color(0xFF1E1E1E)
    val deepPurple = Color(0xFFBB86FC)
    val mediumPurple = Color(0xFF9C64FF)
    val textColor = Color(0xFFFFFFFF)
    val secondaryTextColor = mediumPurple
    val errorColor = Color(0xFFF44336)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(0.5.dp, deepPurple.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = transaction.description,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = textColor
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = dateFormatter.format(Date(transaction.date)),
                            style = MaterialTheme.typography.bodyMedium,
                            color = secondaryTextColor
                        )

                        transaction.category?.let {
                            Text(
                                text = "• ${it.displayName}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = secondaryTextColor
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currencyFormatter.format(transaction.amount),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = errorColor
                    )

                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = errorColor
                        )
                    }
                }
            }
        }
    }
}