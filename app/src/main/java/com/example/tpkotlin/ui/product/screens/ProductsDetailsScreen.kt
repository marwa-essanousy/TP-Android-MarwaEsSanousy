package com.example.tpkotlin.ui.product.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tpkotlin.data.Entities.Product

@Composable
fun ProductDetailsScreen(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = product.productImage),
                contentDescription = product.productTitle,
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp).padding(top = 30.dp)
            )
        }
        Text(
            text = product.productTitle,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(text = "Price: ${product.productPrice} DH", fontSize = 18.sp, color = Color(0xFF009688))
        Text(text = "In Stock: ${product.quantity}", fontSize = 16.sp)
        Text(text = "Category: ${product.category}", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(16.dp))

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
    }
}