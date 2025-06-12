package com.example.tpkotlin.ui.product.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.tpkotlin.data.Entities.Product
import com.example.tpkotlin.ui.cart.CartIntent
import com.example.tpkotlin.ui.cart.CartViewModel
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CartScreen(cartItems: List<Product>, cartViewModel: CartViewModel) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text("Cart", style = MaterialTheme.typography.headlineSmall)

        val groupedItems = cartItems.groupBy { it.productId }

        groupedItems.forEach { (_, products) ->
            val product = products.first()
            val quantity = products.size

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(product.productImage),
                        contentDescription = product.productTitle,
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = product.productTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = "${product.productPrice} DH", color = Color.Gray)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = {
                            if (quantity > 1) {
                                cartViewModel.onIntent(CartIntent.RemoveOne(product))
                            } else {
                                cartViewModel.onIntent(CartIntent.RemoveFromCart(product))
                            }
                        }) {
                            Text("-", fontSize = 18.sp)
                        }

                        Text(quantity.toString(), fontWeight = FontWeight.SemiBold)

                        IconButton(onClick = {
                            cartViewModel.onIntent(CartIntent.AddToCart(product))
                        }) {
                            Text("+", fontSize = 18.sp)
                        }
                    }

                    IconButton(onClick = {
                        cartViewModel.onIntent(CartIntent.RemoveFromCart(product))
                    }) {
                        Text("X", color = Color.Red)
                    }
                }
            }
        }

        // Totaux
        val total = groupedItems.flatMap { it.value }.sumOf { it.productPrice.toDouble() }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text("Total: ${"%.2f".format(total)} DH", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* TODO: Add checkout logic */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Checkout")
        }
    }
}
