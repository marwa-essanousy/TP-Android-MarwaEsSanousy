package com.example.tpkotlin.ui.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.tpkotlin.data.Repository.SharedPreferencesManager

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferencesManager = SharedPreferencesManager(application)

    private val _state = mutableStateOf(CartState(cartItems = sharedPreferencesManager.getCartItems()))
    val state: State<CartState> = _state

    fun onIntent(intent: CartIntent) {
        when (intent) {
            is CartIntent.AddToCart -> {
                val updatedCart = _state.value.cartItems + intent.product
                _state.value = _state.value.copy(cartItems = updatedCart)
                sharedPreferencesManager.saveCartItems(updatedCart)
            }

            is CartIntent.RemoveOne -> {
                val updatedCart = _state.value.cartItems.toMutableList()
                val index = updatedCart.indexOfFirst { it.productId == intent.product.productId }
                if (index != -1) {
                    updatedCart.removeAt(index)
                    _state.value = _state.value.copy(cartItems = updatedCart)
                    sharedPreferencesManager.saveCartItems(updatedCart)
                }
            }

            is CartIntent.RemoveFromCart -> {
                val updatedCart = _state.value.cartItems.filterNot { it.productId == intent.product.productId }
                _state.value = _state.value.copy(cartItems = updatedCart)
                sharedPreferencesManager.saveCartItems(updatedCart)
            }
        }
    }

    fun clearCart() {
        _state.value = _state.value.copy(cartItems = emptyList())
        sharedPreferencesManager.clearCart()
    }
}