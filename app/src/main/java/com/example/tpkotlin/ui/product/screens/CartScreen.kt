package com.example.tpkotlin.ui.product.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.tpkotlin.data.Entities.Product
import com.example.tpkotlin.ui.cart.CartIntent
import com.example.tpkotlin.ui.cart.CartViewModel
import com.example.tpkotlin.ui.product.component.MainLayout

@Composable
fun CartScreen(
    navController: NavController,
    cartItems: List<Product>,
    cartViewModel: CartViewModel
) {
    MainLayout(
        navController = navController,
        selectedIndex = 2,
        cartItemCount = cartItems.size,
        onItemSelected = {}
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
        ) {
            if (cartItems.isEmpty()) {
                EmptyCartState(navController)
            } else {
                CartWithItems(cartItems, cartViewModel, navController)
            }
        }
    }
}

@Composable
private fun EmptyCartState(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Empty Cart",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Your cart is empty",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Add some products to get started",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Start Shopping", fontSize = 16.sp)
        }
    }
}

@Composable
private fun CartWithItems(
    cartItems: List<Product>,
    cartViewModel: CartViewModel,
    navController: NavController
) {
    val groupedItems = cartItems.groupBy { it.productId }
    val total = groupedItems.flatMap { it.value }.sumOf { it.productPrice.toDouble() }

    Column(modifier = Modifier.fillMaxSize()) {
        CartHeader()

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(groupedItems.toList()) { (_, products) ->
                val product = products.first()
                val quantity = products.size
                CartItemCard(
                    product = product,
                    quantity = quantity,
                    onQuantityIncrease = { cartViewModel.onIntent(CartIntent.AddToCart(product)) },
                    onQuantityDecrease = {
                        if (quantity > 1) {
                            cartViewModel.onIntent(CartIntent.RemoveOne(product))
                        } else {
                            cartViewModel.onIntent(CartIntent.RemoveFromCart(product))
                        }
                    },
                    onRemove = { cartViewModel.onIntent(CartIntent.RemoveFromCart(product)) }
                )
            }
        }

        CheckoutSection(
            total = total,
            itemCount = cartItems.size,
            navController = navController
        )

    }
}

@Composable
private fun CartHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Shopping Cart",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Review your items",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }

            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Cart",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun CartItemCard(
    product: Product,
    quantity: Int,
    onQuantityIncrease: () -> Unit,
    onQuantityDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageUrl = product.productImages?.firstOrNull()?.url.orEmpty()

            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = product.productTitle,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.productTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold, // Changer de Bold à SemiBold
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${product.productPrice} DH",
                    color = Color(0xFFD21F3C),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .border(1.dp, Color(0xFFE0E0E0), shape = RoundedCornerShape(50))
                        .background(Color.White, shape = RoundedCornerShape(50))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // "-" bouton
                    Text(
                        text = "-",
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .clickable { onQuantityDecrease() } // action de décrément
                    )

                    Text(
                        text = quantity.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )

                    // "+" bouton
                    Text(
                        text = "+",
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .clickable { onQuantityIncrease() } // action d'incrément
                    )
                }


            }

            // Remove icon
            IconButton(
                onClick = onRemove,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
private fun CheckoutSection(
    total: Double,
    itemCount: Int,
    navController: NavController

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Items", color = Color.Gray)
                Text("%.2f DH".format(total), color = Color(0xFFCC0000), fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Shipping", color = Color.Gray)
                Text("Free", color = Color(0xFF008000), fontWeight = FontWeight.Medium)
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total", fontWeight = FontWeight.Bold)
                Text("%.2f DH".format(total), color = Color(0xFFCC0000), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {navController.navigate("user_info")},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Pay", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
