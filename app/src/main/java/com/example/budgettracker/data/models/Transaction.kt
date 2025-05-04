package com.example.budgettracker.data.models

import com.google.firebase.database.IgnoreExtraProperties
import java.util.Date

enum class TransactionType {
    INCOME,
    EXPENSE
}

enum class ExpenseCategory(val displayName: String) {
    GROCERIES("Groceries"),
    BILLS("Bills"),
    ENTERTAINMENT("Entertainment"),
    TRANSPORT("Transport"),
    HEALTH("Health"),
    EDUCATION("Education"),
    OTHER("Other")
}

@IgnoreExtraProperties
data class Transaction(
    val id: String = "",
    val amount: Double = 0.0,
    val description: String = "",
    val date: Long = Date().time,
    val type: TransactionType = TransactionType.EXPENSE,
    val category: ExpenseCategory? = null,
    val userId: String = ""
) {
    // Empty constructor required for Firebase
    constructor() : this(
        id = "",
        amount = 0.0,
        description = "",
        date = Date().time,
        type = TransactionType.EXPENSE,
        category = null,
        userId = ""
    )
}
