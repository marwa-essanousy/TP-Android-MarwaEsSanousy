package com.example.tpkotlin.ui.cart


import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.tpkotlin.data.Entities.Product

class CartViewModel : ViewModel() {
    private val _state = mutableStateOf(CartState())
    val state: State<CartState> = _state

    fun onIntent(intent: CartIntent) {
        when (intent) {
            is CartIntent.AddToCart -> {
                val updatedCart = _state.value.cartItems + intent.product
                _state.value = _state.value.copy(cartItems = updatedCart)
            }
        }
    }
}
