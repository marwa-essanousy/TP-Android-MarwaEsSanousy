package com.example.tpkotlin.ui.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpkotlin.data.Entities.Product
import com.example.tpkotlin.data.Repository.ProductRepository
import com.example.tpkotlin.data.Repository.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    application: Application
) : AndroidViewModel(application) {

    private val sharedPreferencesManager = SharedPreferencesManager(application)
    private val _state = MutableStateFlow(ProductViewState())
    val state: StateFlow<ProductViewState> = _state

    private val _username = MutableStateFlow(sharedPreferencesManager.getUsername() ?: "Guest")
    val username: StateFlow<String> = _username

    val favoriteProducts: List<Product>
        get() = state.value.products.filter { it.isFavorite }

    fun toggleFavorite(productId: Int) {
        val currentProducts = _state.value.products
        val updatedProducts = currentProducts.map { product ->
            if (product.productId == productId) {
                product.copy(isFavorite = !product.isFavorite)
            } else {
                product
            }
        }

        _state.value = _state.value.copy(products = updatedProducts)

        // Save favorite product IDs to SharedPreferences
        val favoriteIds = updatedProducts.filter { it.isFavorite }.map { it.productId }.toSet()
        sharedPreferencesManager.saveFavoriteProductIds(favoriteIds)
    }

    fun handleIntent(intent: ProductIntent) {
        when (intent) {
            is ProductIntent.LoadProducts -> {
                viewModelScope.launch {
                    loadProducts()
                }
            }
        }
    }

    private suspend fun loadProducts() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val products = repository.getProducts()

            // Load favorite product IDs from SharedPreferences
            val favoriteProductIds = sharedPreferencesManager.getFavoriteProductIds()

            // Update products with favorite status
            val productsWithFavorites = products.map { product ->
                product.copy(isFavorite = favoriteProductIds.contains(product.productId))
            }

            _state.value = ProductViewState(
                isLoading = false,
                products = productsWithFavorites
            )
        } catch (e: Exception) {
            _state.value = ProductViewState(
                isLoading = false,
                error = e.message ?: "Error while loading products."
            )
        }
    }

    fun clearFavorites() {
        val updatedProducts = _state.value.products.map { it.copy(isFavorite = false) }
        _state.value = _state.value.copy(products = updatedProducts)
        sharedPreferencesManager.clearFavorites()
    }

    fun updateUsername(newUsername: String) {
        _username.value = newUsername
    }
}