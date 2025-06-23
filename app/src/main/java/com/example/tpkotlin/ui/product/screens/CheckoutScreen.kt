package com.example.tpkotlin.ui.product.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tpkotlin.data.Entities.Order
import com.example.tpkotlin.data.Entities.Product
import com.example.tpkotlin.data.Repository.SharedPreferencesManager
import com.example.tpkotlin.ui.cart.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    cartItems: List<Product>,
    cartViewModel: CartViewModel,
    sharedPreferencesManager: SharedPreferencesManager
) {
    var fullName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var isPaymentCompleted by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    if (isPaymentCompleted) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "Success",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(96.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Thank you for your order!",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(bottom = 12.dp)
            ) {
                Text("Back to Home")
            }

            Button(
                onClick = {
                    navController.navigate("orders") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.6f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
            ) {
                Text("View Orders", color = Color.White)
            }
        }
    }
    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text("Payment Details", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            // Logos de paiement (placeholders)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PaymentLogo(
                    color = Color(0xFF003087),
                    text = "PayPal"
                )
                PaymentLogo(
                    color = Color(0xFF1A1F71),
                    text = "Visa"
                )
                PaymentLogo(
                    color = Color(0xFFE60023),
                    text = "MasterCard"
                )
                PaymentLogo(
                    color = Color(0xFF0072CE),
                    text = "AmEx"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = cardNumber,
                onValueChange = {
                    // Autoriser seulement chiffres et espaces, max 19 chars (ex: "1234 5678 9012 3456")
                    if (it.length <= 19 && it.all { c -> c.isDigit() || c == ' ' }) cardNumber = it
                },
                label = { Text("Card Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = {
                        if (it.length <= 5) expiryDate = it // Format MM/YY
                    },
                    label = { Text("Expiry Date (MM/YY)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = cvv,
                    onValueChange = {
                        if (it.length <= 4 && it.all { c -> c.isDigit() }) cvv = it
                    },
                    label = { Text("CVV") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (showError) {
                Text(
                    "Please fill in all fields correctly.",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = {
                    if (fullName.isBlank() || cardNumber.length < 12 || expiryDate.length != 5 || cvv.length !in 3..4) {
                        showError = true
                    } else {
                        showError = false

                        val orderId = System.currentTimeMillis().toInt()
                        val orderDate = java.time.LocalDate.now().toString()
                        val totalAmount: Double = cartItems.sumOf { it.productPrice.toDouble() }

                        val newOrder = Order(
                            id = orderId,
                            date = orderDate,
                            totalAmount = totalAmount,
                            status = "Completed"
                        )

                        // Save order to SharedPreferences
                        sharedPreferencesManager.addOrder(newOrder)

                        // Clear cart
                        cartViewModel.clearCart()

                        // Show success UI
                        isPaymentCompleted = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0070BA))
            ) {
                Text("Confirm Payment", color = Color.White, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun PaymentLogo(color: Color, text: String) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, style = MaterialTheme.typography.bodyMedium)
    }
}
