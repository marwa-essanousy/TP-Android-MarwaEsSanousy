package com.example.tpkotlin.ui.product.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tpkotlin.ui.product.ProductViewModel
import com.example.tpkotlin.ui.product.component.MainLayout
import com.example.tpkotlin.ui.product.component.ProductsList

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: ProductViewModel,
    cartItemCount: Int = 0,
    onNavigateToDetails: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val favoriteProducts = state.products.filter { it.isFavorite }

    MainLayout(
        navController = navController,
        selectedIndex = 1,
        cartItemCount = cartItemCount,
        onItemSelected = { /* Handled in MainLayout */ }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Favorites",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )

                if (favoriteProducts.isNotEmpty()) {
                    IconButton(
                        onClick = { viewModel.clearFavorites() }

                    ) {
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (favoriteProducts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No favorite products yet.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap the heart icon on products to add them to favorites",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                ProductsList(
                    products = favoriteProducts,
                    onNavigateToDetails = onNavigateToDetails,
                    onFavoriteClick = onFavoriteClick
                )
            }
        }
    }
}