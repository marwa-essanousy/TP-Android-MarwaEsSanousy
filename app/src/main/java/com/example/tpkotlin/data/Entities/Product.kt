package com.example.tpkotlin.data.Entities

import androidx.annotation.DrawableRes


data class Product(
    val _id: String,
    val productId: Int,
    val productTitle: String,
    @DrawableRes val productImage:Int,
    val productPrice: Int,
    val quantity: Int,
    val category: String,
    val description: String
)