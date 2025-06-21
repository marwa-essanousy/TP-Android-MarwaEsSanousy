package com.example.tpkotlin.ui.product.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tpkotlin.data.Entities.Product
import com.example.tpkotlin.ui.product.ProductViewModel
import com.example.tpkotlin.ui.product.component.ProductsList

@Composable
fun FavoritesScreen(
    viewModel: ProductViewModel,
    onNavigateToDetails: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val favoriteProducts = state.products.filter { it.isFavorite }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Favorites", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (favoriteProducts.isEmpty()) {
            Text("No favorite products yet.")
        } else {
            ProductsList(
                products = favoriteProducts,
                onNavigateToDetails = onNavigateToDetails,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}
