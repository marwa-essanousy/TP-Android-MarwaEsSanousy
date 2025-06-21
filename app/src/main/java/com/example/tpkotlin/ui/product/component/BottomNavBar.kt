package com.example.tpkotlin.ui.product.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavBar(
    selectedIndex: Int = 0,
    cartItemCount: Int = 0,
    onItemSelected: (Int) -> Unit
) {
    val items = listOf("Home", "Favorites", "Cart", "Profile")
    val icons = listOf(Icons.Default.Home, Icons.Default.Favorite, Icons.Default.ShoppingCart, Icons.Default.Person)

    NavigationBar(containerColor = Color.White) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Box {
                        Icon(icons[index], contentDescription = item)
                        // Show cart badge only for cart tab
                        if (index == 2 && cartItemCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(Color.Red, CircleShape)
                                    .align(Alignment.TopEnd),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (cartItemCount > 99) "99+" else cartItemCount.toString(),
                                    color = Color.White,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                },
                label = { Text(item) },
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) }
            )
        }
    }
}