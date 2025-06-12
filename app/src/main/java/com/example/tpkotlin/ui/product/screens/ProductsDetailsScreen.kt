package com.example.tpkotlin.ui.product.screens

import androidx.compose.foundation.Image
import coil.compose.rememberImagePainter
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tpkotlin.data.Entities.Product
import com.example.tpkotlin.ui.cart.CartIntent
import com.example.tpkotlin.ui.cart.CartViewModel

@Composable
fun ProductDetailsScreen(product: Product, onBack: () -> Unit = {}, cartViewModel: CartViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onBack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            IconButton(onClick = {  },  modifier = Modifier.padding(top = 24.dp) ) {
                Box {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color.Red, shape = CircleShape)
                            .align(Alignment.TopEnd)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFF6F6F6)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberImagePainter(product.productImage),
                contentDescription = product.productTitle,
                modifier = Modifier.size(180.dp).clip(RoundedCornerShape(24.dp))
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = product.productTitle,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "In Stock",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = if (product.quantity > 0) "${product.quantity} available" else "Out of stock",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = if (product.quantity > 0) Color.Unspecified else Color.Red
                )
            }

            Column {
                Text(
                    text = "Category",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = product.category,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Description",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
        Text(
            text = product.description,
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 6.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

//        Text(
//            text = "Available Colors",
//            fontWeight = FontWeight.Medium,
//            fontSize = 16.sp
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(12.dp),
//            modifier = Modifier.padding(bottom = 24.dp)
//        ) {
//            ColorCircle(Color(0xFFFF9800))
//            ColorCircle(Color.Black)
//            ColorCircle(Color(0xFFFFF176))
//            ColorCircle(Color(0xFFFFF3E0))
//        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Price",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = "${product.productPrice} DH",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = {
                    cartViewModel.onIntent(CartIntent.AddToCart(product))
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.width(150.dp).height(48.dp)
            ) {
                Text("Add to cart", fontSize = 16.sp)
            }

        }
    }
}

@Composable
private fun ColorCircle(color: Color) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(color)
            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), CircleShape)
    )
}