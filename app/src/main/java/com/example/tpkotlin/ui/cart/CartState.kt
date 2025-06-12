package com.example.tpkotlin.ui.cart


import com.example.tpkotlin.data.Entities.Product

data class CartState(
    val cartItems: List<Product> = emptyList()
)
