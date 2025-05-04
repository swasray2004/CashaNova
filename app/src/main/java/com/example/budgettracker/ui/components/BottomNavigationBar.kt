package com.example.budgettracker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.budgettracker.R
import com.example.budgettracker.ui.theme.BudgetTrackerTheme

@Composable
fun BottomNavigationBarContent(navController: NavController, currentRoute: String?) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Dashboard") },
            label = { Text("Dashboard") },
            selected = currentRoute == "dashboard",
            onClick = {
                if (currentRoute != "dashboard") {
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Add, contentDescription = "Income") },
            label = { Text("Income") },
            selected = currentRoute == "income",
            onClick = {
                if (currentRoute != "income") {
                    navController.navigate("income")
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Delete, contentDescription = "Expense") },
            label = { Text("Expense") },
            selected = currentRoute == "expense",
            onClick = {
                if (currentRoute != "expense") {
                    navController.navigate("expense")
                }
            }
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String?) {
    BottomNavigationBarContent(navController, currentRoute)
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    BudgetTrackerTheme {
        BottomNavigationBarContent(currentRoute = "dashboard", navController = NavController(LocalContext.current))
    }
}
