package com.example.tpkotlin.ui.product.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.example.tpkotlin.data.Entities.Product

@Composable
fun ProductsList(
    products: List<Product>,
    onNavigateToDetails: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit
) {

    var favoriteIds by remember { mutableStateOf(setOf<Int>()) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            ProductItem(product = product,
                onNavigateToDetails = onNavigateToDetails,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}