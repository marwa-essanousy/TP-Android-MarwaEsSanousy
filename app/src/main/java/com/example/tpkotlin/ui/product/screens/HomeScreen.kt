package com.example.tpkotlin.ui.product.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tpkotlin.ui.product.ProductIntent
import com.example.tpkotlin.ui.product.ProductViewModel
import com.example.tpkotlin.ui.product.component.BottomNavBar
import com.example.tpkotlin.ui.product.component.ProductsList

@Composable
fun HomeScreen(
    viewModel: ProductViewModel = viewModel(),
    onNavigateToDetails: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    onProfileClick: () -> Unit,
    onCartClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var selectedIndex by remember { mutableStateOf(0) }
    val categories = listOf("All", "accessories", "clothing", "shoes")
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(ProductIntent.LoadProducts)
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    selectedIndex = index
                    when (index) {
                        0 -> {} // Home
                        2 -> onCartClick()
                        3 -> onProfileClick()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(categories) { category ->
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (category == selectedCategory) MaterialTheme.colorScheme.primary else Color.Gray,
                        modifier = Modifier
                            .clickable { selectedCategory = category }
                            .padding(vertical = 8.dp)
                    )
                }
            }

            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                state.error != null -> {
                    Text(
                        text = "Error: ${state.error}",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    ProductsList(
                        products = if (selectedCategory == "All") state.products
                        else state.products.filter { it.category == selectedCategory },
                        onNavigateToDetails = onNavigateToDetails,
                        onFavoriteClick = onFavoriteClick
                    )
                }
            }
        }
    }
}