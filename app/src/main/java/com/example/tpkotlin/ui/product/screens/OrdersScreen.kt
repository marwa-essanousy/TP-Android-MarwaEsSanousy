package com.example.tpkotlin.ui.product.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tpkotlin.data.Repository.SharedPreferencesManager
import com.example.tpkotlin.data.Entities.Order
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPrefsManager = remember { SharedPreferencesManager(context) }

    var orders by remember { mutableStateOf(listOf<Order>()) }

    LaunchedEffect(Unit) {
        orders = sharedPrefsManager.getOrders()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Orders") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
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
                Text("You have no orders yet.")
            }
        } else {
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier.fillMaxSize()
            ) {
                items(orders) { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Order ID: ${order.id}", style = MaterialTheme.typography.titleMedium)
                            Text("Date: ${order.date}", style = MaterialTheme.typography.bodyMedium)
                            Text("Total: ${order.totalAmount} DAM", style = MaterialTheme.typography.bodyMedium)
                            Text("Status: ${order.status}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
