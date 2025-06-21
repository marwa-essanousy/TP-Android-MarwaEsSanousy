package com.example.tpkotlin.ui.cart

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State


class CartViewModel : ViewModel() {
    private val _state = mutableStateOf(CartState())
    val state: State<CartState> = _state

    fun onIntent(intent: CartIntent) {
        when (intent) {
            is CartIntent.AddToCart -> {
                val updatedCart = _state.value.cartItems + intent.product
                _state.value = _state.value.copy(cartItems = updatedCart)
            }

            is CartIntent.RemoveOne -> {
                val updatedCart = _state.value.cartItems.toMutableList()
                val index = updatedCart.indexOfFirst { it.productId == intent.product.productId }
                if (index != -1) {
                    updatedCart.removeAt(index)
                    _state.value = _state.value.copy(cartItems = updatedCart)
                }
            }

            is CartIntent.RemoveFromCart -> {
                val updatedCart = _state.value.cartItems.filterNot { it.productId == intent.product.productId }
                _state.value = _state.value.copy(cartItems = updatedCart)
            }
        }
    }
}