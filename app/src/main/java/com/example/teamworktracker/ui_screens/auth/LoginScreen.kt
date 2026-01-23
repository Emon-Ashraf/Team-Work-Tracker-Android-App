package com.example.teamworktracker.ui_screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teamworktracker.R
import com.example.teamworktracker.ui.theme.AppColors
import com.example.teamworktracker.ui.theme.TeamWorkTrackerTheme

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val email by viewModel.email
    val password by viewModel.password
    val errorMessage by viewModel.errorMessage
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.success) {
        if (state.success) onLoginSuccess()
    }

    LoginScreenContent(
        email = email,
        onEmailChange = { viewModel.email.value = it },
        password = password,
        onPasswordChange = { viewModel.password.value = it },
        errorMessage = errorMessage,
        state = state,
        onLogin = { viewModel.login() },
        onGoToRegister = onGoToRegister
    )
}

@Composable
fun LoginScreenContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    errorMessage: String?,
    state: LoginUiState,
    onLogin: () -> Unit,
    onGoToRegister: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(containerColor = AppColors.BgDark) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0B0F2B),
                            AppColors.BgDark,
                            AppColors.BgDark
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 520.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = AppColors.CardDark),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFF10163A)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.app_logo),
                                contentDescription = "App Logo",
                                modifier = Modifier.size(54.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Team Work Tracker",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Welcome back, please login to continue.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.MutedText
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        Text(
                            text = "Email Address",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = onEmailChange,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !state.loading,
                            singleLine = true,
                            placeholder = { Text("name@company.com", color = AppColors.MutedText) },
                            leadingIcon = {
                                Icon(Icons.Filled.Email, contentDescription = null, tint = AppColors.MutedText)
                            },
                            colors = fieldColors(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Password",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = onPasswordChange,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !state.loading,
                            singleLine = true,
                            placeholder = { Text("Enter your password", color = AppColors.MutedText) },
                            leadingIcon = {
                                Icon(Icons.Filled.Lock, contentDescription = null, tint = AppColors.MutedText)
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(
                                    onClick = { passwordVisible = !passwordVisible },
                                    enabled = !state.loading
                                ) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                        tint = AppColors.MutedText
                                    )
                                }
                            },
                            colors = fieldColors(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        // UI only, no new behavior -> disabled
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = { /* UI only */ },
                                enabled = false,
                                contentPadding = PaddingValues(horizontal = 0.dp)
                            ) {
                                Text("Forgot Password?", color = AppColors.PrimaryBlue)
                            }
                        }

                        errorMessage?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .padding(top = 6.dp)
                                    .fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (state.loading) {
                            CircularProgressIndicator(color = AppColors.PrimaryBlue)
                        } else {
                            Button(
                                onClick = onLogin,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppColors.PrimaryBlue,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Log In")
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        TextButton(onClick = onGoToRegister, enabled = !state.loading) {
                            Text("Don't have an account? ", color = AppColors.MutedText)
                            Text("Register", color = AppColors.PrimaryBlue)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = AppColors.FieldBg,
    unfocusedContainerColor = AppColors.FieldBg,
    disabledContainerColor = AppColors.FieldBg.copy(alpha = 0.6f),

    focusedBorderColor = AppColors.PrimaryBlue,
    unfocusedBorderColor = AppColors.Border,
    disabledBorderColor = AppColors.Border.copy(alpha = 0.5f),

    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    disabledTextColor = Color.White.copy(alpha = 0.7f),

    cursorColor = AppColors.PrimaryBlue
)

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    TeamWorkTrackerTheme {
        LoginScreenContent(
            email = "user@example.com",
            onEmailChange = {},
            password = "password123",
            onPasswordChange = {},
            errorMessage = null,
            state = LoginUiState(),
            onLogin = {},
            onGoToRegister = {}
        )
    }
}
