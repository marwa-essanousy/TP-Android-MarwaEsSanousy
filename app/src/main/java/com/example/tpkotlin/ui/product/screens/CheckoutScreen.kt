package com.example.tpkotlin.ui.product.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tpkotlin.R
import com.example.tpkotlin.data.Entities.Order
import com.example.tpkotlin.data.Entities.Product
import com.example.tpkotlin.data.Repository.SharedPreferencesManager
import com.example.tpkotlin.ui.cart.CartViewModel
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import android.util.Patterns
import coil.compose.AsyncImage

sealed class PaymentMethod(val name: String, val icon: Int) {
    object PayPal : PaymentMethod("PayPal", R.drawable.paypal)
    object Visa : PaymentMethod("Visa", R.drawable.visa)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    cartItems: List<Product>,
    cartViewModel: CartViewModel,
    sharedPreferencesManager: SharedPreferencesManager,
    userFullName: String,
    userPhone: String,
    userAddress: String
) {
    var fullName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var paypalEmail by remember { mutableStateOf("") }
    var isPaymentCompleted by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var selectedPaymentMethod by remember { mutableStateOf<PaymentMethod?>(null) }

    val context = LocalContext.current
    val paymentMethods = listOf(PaymentMethod.PayPal, PaymentMethod.Visa)

    if (isPaymentCompleted) {
        PaymentSuccessScreen(navController)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
                Text("Checkout",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 32.dp, bottom = 8.dp)
                )
            Divider(modifier = Modifier.padding(vertical = 2.dp))
            Text(
                "Order Summary",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            cartItems.forEach { product ->
                OrderItemRow(product)
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", style = MaterialTheme.typography.titleMedium)
                Text(
                    "${"%.2f".format(cartItems.sumOf { it.productPrice.toDouble() })} DH",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                "Payment Method",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(vertical = 8.dp)
            )


            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(paymentMethods) { method ->
                    PaymentMethodCard(
                        method = method,
                        isSelected = selectedPaymentMethod == method,
                        onSelected = { selectedPaymentMethod = method }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Dynamic Form based on selected payment method
            when (selectedPaymentMethod) {
                PaymentMethod.PayPal -> {
                    OutlinedTextField(
                        value = paypalEmail,
                        onValueChange = { paypalEmail = it },
                        label = { Text("PayPal Email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = paypalEmail.isNotBlank() && !isValidEmail(paypalEmail)
                    )

                    if (paypalEmail.isNotBlank() && !isValidEmail(paypalEmail)) {
                        Text(
                            "Please enter a valid email address",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                PaymentMethod.Visa -> {
                    CreditCardForm(
                        fullName = fullName,
                        cardNumber = cardNumber,
                        expiryDate = expiryDate,
                        cvv = cvv,
                        onFullNameChange = { fullName = it },
                        onCardNumberChange = { cardNumber = it },
                        onExpiryDateChange = { expiryDate = it },
                        onCvvChange = { cvv = it }
                    )
                }

                null -> {
                    // No payment method selected
                    Text(
                        "Please select a payment method",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                }
            }

            // Payment Button
            Button(
                onClick = {
                    val isValid = when (selectedPaymentMethod) {
                        PaymentMethod.PayPal -> {
                            if (paypalEmail.isBlank() || !isValidEmail(paypalEmail)) {
                                Toast.makeText(context, "Please enter a valid PayPal email", Toast.LENGTH_SHORT).show()
                                false
                            } else true
                        }

                        PaymentMethod.Visa -> {
                            validateForm(context, selectedPaymentMethod, fullName, cardNumber, expiryDate, cvv)
                        }

                        null -> {
                            Toast.makeText(context, "Please select a payment method", Toast.LENGTH_SHORT).show()
                            false
                        }
                    }

                    if (isValid) {
                        processPayment(
                            cartItems = cartItems,
                            cartViewModel = cartViewModel,
                            sharedPreferencesManager = sharedPreferencesManager
                        ) {
                            isPaymentCompleted = true
                        }
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) { Text(
                when (selectedPaymentMethod) {
                    PaymentMethod.PayPal -> "Pay with PayPal"
                    else -> "Confirm Payment"
                }
            )
            }
        }
    }
}

private fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
@Composable
fun PaymentSuccessScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                .border(
                    BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "Success",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Payment Successful!",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Your order has been placed successfully. You will receive a confirmation email shortly.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("home") { popUpTo("home") { inclusive = true } } },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Back to Home")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { navController.navigate("orders") { popUpTo("home") { inclusive = false } } },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(48.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("View Orders")
        }
    }
}

@Composable
fun OrderItemRow(product: Product) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = product.productImages?.firstOrNull()?.url,
            contentDescription = product.productTitle,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,

        )


        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                product.productTitle,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
            Text(
                "${"%.2f".format(product.productPrice.toDouble())} DAM",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }

        Text(
            "Qty: 1",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun PaymentMethodCard(
    method: PaymentMethod,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent

    Box(
        modifier = Modifier
            .width(100.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onSelected() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = method.icon),
            contentDescription = method.name,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
fun CreditCardForm(
    fullName: String,
    cardNumber: String,
    expiryDate: String,
    cvv: String,
    onFullNameChange: (String) -> Unit,
    onCardNumberChange: (String) -> Unit,
    onExpiryDateChange: (String) -> Unit,
    onCvvChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        OutlinedTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            label = { Text("fullName ") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = cardNumber,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() || it == ' ' } && newValue.length <= 19) {
                    onCardNumberChange(newValue)
                }
            },
            label = { Text("cardNumber") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = expiryDate,
                onValueChange = { newValue ->
                    if (newValue.length <= 5) {
                        val cleaned = newValue.filter { it.isDigit() }
                        when {
                            cleaned.isEmpty() -> onExpiryDateChange("")
                            cleaned.length <= 2 -> {
                                val month = cleaned.take(2)
                                if (month.toIntOrNull() in 1..12) {
                                    onExpiryDateChange(month)
                                }
                            }
                            else -> {
                                val month = cleaned.take(2)
                                val year = cleaned.drop(2).take(2)
                                if (month.toIntOrNull() in 1..12) {
                                    onExpiryDateChange("$month/$year")
                                }
                            }
                        }
                    }
                },
                label = { Text("Expiration (MM/AA)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                isError = !isValidDate(expiryDate) && expiryDate.isNotBlank()
            )

            OutlinedTextField(
                value = cvv,
                onValueChange = { newValue ->
                    if (newValue.length <= 3 && newValue.all { it.isDigit() }) {
                        onCvvChange(newValue)
                    }
                },
                label = { Text("CVV") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

private fun validateForm(
    context: Context,
    paymentMethod: PaymentMethod?,
    fullName: String,
    cardNumber: String,
    expiryDate: String,
    cvv: String
): Boolean {
    if (paymentMethod == null) {
        Toast.makeText(context, "Veuillez sélectionner un mode de paiement", Toast.LENGTH_SHORT).show()
        return false
    }

    return when (paymentMethod) {
        is PaymentMethod.PayPal -> true

        is PaymentMethod.Visa -> {
            val cleanedCardNumber = cardNumber.replace(" ", "")

            when {
                fullName.isBlank() -> {
                    Toast.makeText(context, "Veuillez entrer le nom du titulaire", Toast.LENGTH_SHORT).show()
                    false
                }
                cleanedCardNumber.length != 16 -> {
                    Toast.makeText(context, "Le numéro de carte doit contenir 16 chiffres", Toast.LENGTH_SHORT).show()
                    false
                }
                !cleanedCardNumber.startsWith("4") -> {
                    Toast.makeText(context, "Numéro de carte Visa invalide (doit commencer par 4)", Toast.LENGTH_SHORT).show()
                    false
                }
                expiryDate.length != 5 -> {
                    Toast.makeText(context, "Date d'expiration invalide (format MM/AA requis)", Toast.LENGTH_SHORT).show()
                    false
                }
                cvv.length != 3 -> {
                    Toast.makeText(context, "Le CVV doit contenir exactement 3 chiffres", Toast.LENGTH_SHORT).show()
                    false
                }
                else -> true
            }
        }
    }
}
private fun processPayment(
    cartItems: List<Product>,
    cartViewModel: CartViewModel,
    sharedPreferencesManager: SharedPreferencesManager,
    onSuccess: () -> Unit
) {
    val orderId = System.currentTimeMillis().toInt()
    val orderDate = java.time.LocalDate.now().toString()
    val totalAmount: Double = cartItems.sumOf { it.productPrice.toDouble() }

    val newOrder = Order(
        id = orderId,
        date = orderDate,
        totalAmount = totalAmount.toInt() ,
        status = "Completed"
    )

    sharedPreferencesManager.addOrder(newOrder)

    cartViewModel.clearCart()

    onSuccess()
}

private fun isValidDate(date: String): Boolean {
    if (date.length != 5 || date[2] != '/') return false

    val (monthStr, yearStr) = date.split("/")
    val month = monthStr.toIntOrNull() ?: return false
    val year = yearStr.toIntOrNull() ?: return false

    val currentYear = java.time.LocalDate.now().year % 100
    return month in 1..12 && year >= currentYear
}


