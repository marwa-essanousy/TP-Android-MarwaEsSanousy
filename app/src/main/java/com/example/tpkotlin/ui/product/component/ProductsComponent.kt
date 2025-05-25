package com.example.tpkotlin.ui.product.component

import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import  com.example.tpkotlin.data.Entities.Product


@Composable
fun ProductsList(
    products: List<Product>,
    onNavigateToDetails: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit
) {
    var favoriteIds by remember { mutableStateOf(setOf<Int>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(products) { product ->
                ProductItem(
                    product = product,
                    onNavigateToDetails = onNavigateToDetails,
                    onFavoriteClick = {
                        favoriteIds = if (favoriteIds.contains(product.productId)) {
                            favoriteIds - product.productId
                        } else {
                            favoriteIds + product.productId
                        }
                        onFavoriteClick(product.productId)
                    },
                    isFavorite = favoriteIds.contains(product.productId)
                )
            }
        }
    }
}