//package com.example.tpkotlin.ui.product.screens
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.example.tpkotlin.ui.product.AuthState
//import com.example.tpkotlin.ui.product.AuthViewModel
//
//@Composable
//fun ProfileScreen(
//    viewModel: AuthViewModel = remember { AuthViewModel() }
//) {
//    val authState by viewModel.authState.collectAsState()
//    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
//
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        if (isLoggedIn && authState is AuthState.Success) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Text(
//                    text = (authState as AuthState.Success).message,
//                    style = MaterialTheme.typography.headlineMedium
//                )
//                Spacer(modifier = Modifier.height(24.dp))
//                Button(onClick = { viewModel.logout() }) {
//                    Text("Déconnexion")
//                }
//            }
//        } else {
//            Text(
//                text = "Vous n'êtes pas connecté",
//                style = MaterialTheme.typography.bodyLarge
//            )
//        }
//    }
//}
