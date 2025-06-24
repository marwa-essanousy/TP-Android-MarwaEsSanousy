package com.example.tpkotlin.ui.product.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tpkotlin.data.Repository.SharedPreferencesManager
import com.example.tpkotlin.data.Entities.Order
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.tpkotlin.data.Entities.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPrefsManager = remember { SharedPreferencesManager(context) }

    var orders by remember { mutableStateOf(listOf<Order>()) }

    LaunchedEffect(Unit) {
        orders = sharedPrefsManager.getOrders()
    }

    Scaffold(
        containerColor = Color.White, // Fond blanc pour tout l'écran
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Orders",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 24.sp // Taille personnalisée
                        ) // Texte noir
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black // Icône noire
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White // Fond blanc pour la TopAppBar
                )
            )
        }
    ) { padding ->
        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "You have no orders yet.",
                    style = TextStyle(color = Color.Black) // Texte noir
                )
            }
        } else {
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier.fillMaxSize()
            ) {
                items(orders) { order ->
                    OrderCard(order = order)
                }
        }
    }
}
}


@Composable
fun OrderCard(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Order header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Order #${order.id}",
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.Black)
                )
                Text(
                    order.date,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )

            }


            Spacer(modifier = Modifier.height(12.dp))
//
//            val products = order.products ?: emptyList()
//            val itemsText = when {
//                products.isEmpty() -> "No items"
//                products.size == 1 -> "1 item"
//                else -> "${products.size} items"
//            }
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    "Items: $itemsText",
//                    style = MaterialTheme.typography.titleMedium.copy(
//                        color = Color.Black,
//                        fontWeight = FontWeight.Bold
//                    )
//                )
//                // Option: Ajouter un indicateur visuel si vide
//                if (products.isEmpty()) {
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Icon(
//                        imageVector = Icons.Default.Warning,
//                        contentDescription = "Empty order",
//                        tint = Color(0xFFFFA000), // Orange
//                        modifier = Modifier.size(18.dp)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//
//            if (products.isNotEmpty()) {
//                products.forEachIndexed { index, product ->
//                    ProductItem(product = product)
//                    if (index < products.size - 1) {
//                        Spacer(modifier = Modifier.height(4.dp))
//                    }
//                }
//            } else {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(40.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        "This order contains no items",
//                        style = MaterialTheme.typography.bodyMedium.copy(
//                            color = Color.Gray,
//                            fontStyle = FontStyle.Italic
//                        )
//                    )
//                }
//            }

            Spacer(modifier = Modifier.height(12.dp))

            // Total and status
            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Total Amount:",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "${order.totalAmount} DH",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Status:",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
                )
                Text(
                    order.status.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = when(order.status.lowercase()) {
                            "delivered" -> Color(0xFF388E3C) // Green
                            "cancelled" -> Color(0xFFD32F2F) // Red
                            "shipped" -> Color(0xFF1976D2) // Blue
                            else -> Color(0xFFFB8C00) // Orange for processing
                        },
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "• ${product.productTitle}",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            "${product.quantity} × ${product.productPrice} DH",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
        )
    }
}