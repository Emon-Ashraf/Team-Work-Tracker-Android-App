package com.example.teamworktracker.ui_screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teamworktracker.R
import com.example.teamworktracker.ui.theme.AppColors

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val email by viewModel.email
    val username by viewModel.username
    val fullName by viewModel.fullName
    val password by viewModel.password
    val errorMessage by viewModel.errorMessage

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
            IconButton(
                onClick = onBackToLogin,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

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
                                .size(64.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFF10163A)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.app_logo),
                                contentDescription = "App Logo",
                                modifier = Modifier.size(34.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Create Account",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Sign up to start tracking your team's productivity seamlessly.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.MutedText
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Label("Email Address")
                        Spacer(modifier = Modifier.height(8.dp))
                        AppField(
                            value = email,
                            onValueChange = { viewModel.email.value = it },
                            placeholder = "name@company.com",
                            leading = { Icon(Icons.Filled.Email, null, tint = AppColors.MutedText) }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Kept from your code
                        Label("Username")
                        Spacer(modifier = Modifier.height(8.dp))
                        AppField(
                            value = username,
                            onValueChange = { viewModel.username.value = it },
                            placeholder = "your_username",
                            leading = { Icon(Icons.Filled.Badge, null, tint = AppColors.MutedText) }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Kept from your code
                        Label("Full Name")
                        Spacer(modifier = Modifier.height(8.dp))
                        AppField(
                            value = fullName,
                            onValueChange = { viewModel.fullName.value = it },
                            placeholder = "Your full name",
                            leading = { Icon(Icons.Filled.Person, null, tint = AppColors.MutedText) }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Label("Password")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { viewModel.password.value = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = { Text("Create a password", color = AppColors.MutedText) },
                            leadingIcon = { Icon(Icons.Filled.Lock, null, tint = AppColors.MutedText) },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
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

                        errorMessage?.let {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = {
                                val success = viewModel.register()
                                if (success) onRegisterSuccess()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppColors.PrimaryBlue,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Register")
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Already a member? ", color = AppColors.MutedText)
                            TextButton(
                                onClick = onBackToLogin,
                                contentPadding = PaddingValues(horizontal = 0.dp)
                            ) {
                                Text("Log In", color = AppColors.PrimaryBlue)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun AppField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leading: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = { Text(placeholder, color = AppColors.MutedText) },
        leadingIcon = leading,
        colors = fieldColors(),
        shape = RoundedCornerShape(14.dp)
    )
}
