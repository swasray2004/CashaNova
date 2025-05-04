package com.example.budgettracker.ui.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.budgettracker.ui.viewmodels.AuthState
import com.example.budgettracker.ui.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    // Custom color palette
    val darkBackground = Color(0xFF121212)
    val deepPurple = Color(0xFF6A0DAD)
    val lightPurple = Color(0xFFBB86FC)
    val mediumPurple = Color(0xFF9C27B0)
    val errorColor = Color(0xFFCF6679)
    val textColor = Color.White
    val secondaryTextColor = Color.White.copy(alpha = 0.7f)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }
    var isConfirmPasswordError by remember { mutableStateOf(false) }
    var passwordErrorText by remember { mutableStateOf("") }
    var confirmPasswordErrorText by remember { mutableStateOf("") }

    val authState by authViewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                navController.navigate("dashboard") {
                    popUpTo("signin") { inclusive = true }
                }
                authViewModel.resetAuthState()
            }
            is AuthState.Error -> {
                snackbarHostState.showSnackbar((authState as AuthState.Error).message)
                authViewModel.resetAuthState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Create Account",
                        color = textColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = lightPurple
                        )
                    }
                },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = darkBackground,
                    navigationIconContentColor = lightPurple,
                    titleContentColor = textColor
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = darkBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(darkBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Join CashaNova",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = lightPurple
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Create an account to track your finances",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = secondaryTextColor
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        isEmailError = false
                    },
                    label = { Text("Email", color = textColor) },
                    isError = isEmailError,
                    supportingText = {
                        if (isEmailError) {
                            Text(
                                "Please enter a valid email",
                                color = errorColor
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        disabledTextColor = textColor.copy(alpha = 0.5f),
                        cursorColor = lightPurple,
                        focusedBorderColor = lightPurple,
                        unfocusedBorderColor = secondaryTextColor,
                        errorBorderColor = errorColor,
                        focusedLabelColor = lightPurple,
                        unfocusedLabelColor = secondaryTextColor
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        isPasswordError = false
                    },
                    label = { Text("Password", color = textColor) },
                    isError = isPasswordError,
                    supportingText = {
                        if (isPasswordError) {
                            Text(
                                passwordErrorText,
                                color = errorColor
                            )
                        }
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    colors =androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        disabledTextColor = textColor.copy(alpha = 0.5f),
                        cursorColor = lightPurple,
                        focusedBorderColor = lightPurple,
                        unfocusedBorderColor = secondaryTextColor,
                        errorBorderColor = errorColor,
                        focusedLabelColor = lightPurple,
                        unfocusedLabelColor = secondaryTextColor
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        isConfirmPasswordError = false
                    },
                    label = { Text("Confirm Password", color = textColor) },
                    isError = isConfirmPasswordError,
                    supportingText = {
                        if (isConfirmPasswordError) {
                            Text(
                                confirmPasswordErrorText,
                                color = errorColor
                            )
                        }
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        disabledTextColor = textColor.copy(alpha = 0.5f),
                        cursorColor = lightPurple,
                        focusedBorderColor = lightPurple,
                        unfocusedBorderColor = secondaryTextColor,
                        errorBorderColor = errorColor,
                        focusedLabelColor = lightPurple,
                        unfocusedLabelColor = secondaryTextColor
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        isEmailError = email.isEmpty() || !email.contains("@")

                        if (password.length < 6) {
                            isPasswordError = true
                            passwordErrorText = "Password must be at least 6 characters"
                        } else {
                            isPasswordError = false
                        }

                        if (password != confirmPassword) {
                            isConfirmPasswordError = true
                            confirmPasswordErrorText = "Passwords do not match"
                        } else {
                            isConfirmPasswordError = false
                        }

                        if (!isEmailError && !isPasswordError && !isConfirmPasswordError) {
                            authViewModel.signUp(email, password)
                        }
                    },
                    enabled = authState !is AuthState.Loading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = deepPurple,
                        contentColor = textColor,
                        disabledContainerColor = deepPurple.copy(alpha = 0.5f),
                        disabledContentColor = textColor.copy(alpha = 0.5f)
                    )
                ) {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(end = 8.dp),
                            strokeWidth = 2.dp,
                            color = textColor
                        )
                    }
                    Text("Create Account")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Already have an account? ",
                        color = secondaryTextColor
                    )
                    TextButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Text(
                            text = "Sign In",
                            color = lightPurple,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
