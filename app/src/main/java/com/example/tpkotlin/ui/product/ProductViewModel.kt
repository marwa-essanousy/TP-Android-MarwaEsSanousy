package com.example.tpkotlin.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpkotlin.data.Repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProductViewState())
    val state: StateFlow<ProductViewState> = _state

    fun toggleFavorite(productId: Int) {
        _state.value = _state.value.copy(
            products = _state.value.products.map { product ->
                if (product.productId == productId) {
                    product.copy(isFavorite = !product.isFavorite)
                } else {
                    product
                }
            }
        )
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
            _state.value = ProductViewState(
                isLoading = false,
                products = products
            )
        } catch (e: Exception) {
            _state.value = ProductViewState(
                isLoading = false,
                error = e.message ?: "Erreur lors du chargement des produits"
            )
        }
    }
}
