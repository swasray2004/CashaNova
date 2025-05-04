package com.example.budgettracker.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun SignInScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }

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
            is AuthState.PasswordResetSent -> {
                snackbarHostState.showSnackbar("Password reset email sent")
                authViewModel.resetAuthState()
            }
            else -> {}
        }
    }

    // Custom dark theme colors
    val darkPurple = Color(0xFF6A0DAD)
    val darkerPurple = Color(0xFF4A148C)
    val darkBackground = Color(0xFF121212)
    val lightPurple = Color(0xFFBB86FC)
    val errorColor = Color(0xFFCF6679)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = darkBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "CashaNova",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = lightPurple,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sign in to your account",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White.copy(alpha = 0.7f)
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        isEmailError = false
                    },
                    label = { Text("Email", color = Color.White) },
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
                        cursorColor = lightPurple,
        focusedBorderColor = lightPurple,
        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
        errorBorderColor = errorColor,
        focusedLabelColor = lightPurple,
        unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        isPasswordError = false
                    },
                    label = { Text("Password", color = Color.White) },
                    isError = isPasswordError,
                    supportingText = {
                        if (isPasswordError) {
                            Text(
                                "Password cannot be empty",
                                color = errorColor
                            )
                        }
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                        cursorColor = lightPurple,
        focusedBorderColor = lightPurple,
        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
        errorBorderColor = errorColor,
        focusedLabelColor = lightPurple,
        unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = {
                        if (email.isNotEmpty()) {
                            authViewModel.resetPassword(email)
                        } else {
                            isEmailError = true
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        "Forgot Password?",
                        color = lightPurple
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        isEmailError = email.isEmpty() || !email.contains("@")
                        isPasswordError = password.isEmpty()

                        if (!isEmailError && !isPasswordError) {
                            authViewModel.signIn(email, password)
                        }
                    },
                    enabled = authState !is AuthState.Loading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = darkPurple,
                        contentColor = Color.White,
                        disabledContainerColor = darkPurple.copy(alpha = 0.5f),
                        disabledContentColor = Color.White.copy(alpha = 0.5f)
                    )
                ) {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(end = 8.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    }
                    Text("Sign In")
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = {
                        navController.navigate("signup")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                        contentColor = lightPurple,
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(1.dp, lightPurple)
                ) {
                    Text("Create Account")
                }
            }
        }
    }
}
