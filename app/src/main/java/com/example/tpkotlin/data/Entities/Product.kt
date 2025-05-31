package com.example.tpkotlin.data.Entities




data class Product(
//    val _id: String,
    val productId: Int,
    val productTitle: String,
    val productImage : String,
    val productPrice: Int,
    val quantity: Int,
    val category: String,
    val description: String,
    val isFavorite: Boolean = false
)
