package com.example.tpkotlin.data.Entities


data class Order(
    val id: Int,
    val date: String,
    val totalAmount: Int,
    val status: String,
    val products: List<Product> = emptyList(),
    val paymentMethod: String = ""
)