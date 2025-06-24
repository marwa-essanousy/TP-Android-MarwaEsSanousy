package com.example.tpkotlin.data.Entities
data class ProductImage(
    val color: String,
    val url: String
)

data class Product(
    val productId: Int,
    val productTitle: String,
    val productImages: List<ProductImage>? = null,
    val productPrice: Int,
    val quantity: Int,
    val category: String,
    val description: String,
    val isFavorite: Boolean = false
)
