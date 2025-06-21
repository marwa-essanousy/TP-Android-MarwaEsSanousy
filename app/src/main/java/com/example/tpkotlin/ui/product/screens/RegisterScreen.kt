package com.example.tpkotlin.ui.product.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.tpkotlin.data.Entities.User
import com.example.tpkotlin.ui.product.AuthState
import com.example.tpkotlin.ui.product.AuthViewModel

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = remember { AuthViewModel(context.applicationContext as android.app.Application) }

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val state by viewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is AuthState.Loading -> {
                CircularProgressIndicator()
            }
            is AuthState.Error -> {
                Text(
                    text = (state as AuthState.Error).error,
                    color = MaterialTheme.colorScheme.error
                )
            }
            is AuthState.Success -> {
                Text(
                    text = (state as AuthState.Success).message,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            else -> { /* Nothing to display */ }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val user = User(username.trim(), email.trim(), password)
                viewModel.register(user)
            },
            enabled = username.isNotBlank() && email.isNotBlank() && password.isNotBlank()
        ) {
            Text("Sign Up")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onLoginClick) {
            Text("Already registered? Log in")
        }
    }
}