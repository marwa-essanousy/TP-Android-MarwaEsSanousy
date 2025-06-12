package com.example.tpkotlin.ui.cart


import com.example.tpkotlin.data.Entities.Product

sealed class CartIntent {
    data class AddToCart(val product: Product) : CartIntent()
}
