package com.example.tpkotlin.ui.product.component


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun MainLayout(
    navController: NavController,
    selectedIndex: Int,
    cartItemCount: Int = 0,
    onItemSelected: (Int) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedIndex = selectedIndex,
                cartItemCount = cartItemCount,
                onItemSelected = { index ->
                    onItemSelected(index)
                    when (index) {
                        0 -> navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                        1 -> navController.navigate("favorites") {
                            popUpTo("home") { inclusive = true }
                        }
                        2 -> navController.navigate("cart") {
                            popUpTo("home") { inclusive = true }
                        }
                        3 -> navController.navigate("profile") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}