package com.example.tpkotlin.ui.product.screens

import android.R.attr.onClick
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.tpkotlin.data.Entities.Product
import com.example.tpkotlin.ui.cart.CartIntent
import com.example.tpkotlin.ui.cart.CartViewModel
import kotlinx.coroutines.delay
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
@Composable
fun ProductDetailsScreen(
    product: Product,
    onBack: () -> Unit = {},
    cartViewModel: CartViewModel
) {
    var showAddedToCartMessage by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) } // État pour le favoris

    val selectedColor = remember {
        mutableStateOf(product.productImages?.firstOrNull()?.color ?: "")
    }

    val selectedImage = remember {
        mutableStateOf(product.productImages?.firstOrNull()?.url ?: "")
    }

    LaunchedEffect(showAddedToCartMessage) {
        if (showAddedToCartMessage) {
            delay(2000)
            showAddedToCartMessage = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 8.dp).padding(top= 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onBack() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = Color.Black)
                }

                IconButton(
                    onClick = { /* Cart Click */ },
                    modifier = Modifier.size(48.dp)
                ) {
                    Box {
                        Icon(Icons.Default.ShoppingCart, "Cart", tint = Color.Black)
                        // Badge simple (sans count)
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color.Red, shape = CircleShape)
                                .align(Alignment.TopEnd)
                        )
                    }
                }
            }
            // Image du produit
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFF6F6F6)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberImagePainter(selectedImage.value),
                    contentDescription = product.productTitle,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(24.dp))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Titre avec bouton favoris
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.productTitle,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { isFavorite = !isFavorite }
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favoris",
                        tint = if (isFavorite) Color.Black else Color.Gray

                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.description,
                fontSize = 15.sp,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stock et catégorie
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
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sélecteur de couleur
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                product.productImages?.forEach { image ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(android.graphics.Color.parseColor(image.color)))
                            .border(
                                width = 2.dp,
                                color = if (selectedColor.value == image.color) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                selectedColor.value = image.color
                                selectedImage.value = image.url
                            }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Prix et bouton Ajouter au panier
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
                        fontSize = 22.sp
                    )
                }

                Button(
                    onClick = {
                        cartViewModel.onIntent(CartIntent.AddToCart(product))
                        showAddedToCartMessage = true
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .width(150.dp)
                        .height(48.dp),
                    enabled = product.quantity > 0
                ) {
                    Text("Add to cart", fontSize = 16.sp)
                }
            }
        }

        // Message "Added to cart"
        if (showAddedToCartMessage) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 100.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(
                    text = "Added to cart!",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}