package com.example.tpkotlin.ui.product.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tpkotlin.data.Entities.Product

@Composable
fun CartScreen(cartItems: List<Product>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Your Cart", style = MaterialTheme.typography.headlineSmall)

        cartItems.forEach { product ->
            Text("${product.productTitle} - ${product.productPrice} DH")
        }
    }
}
