package com.example.tpkotlin.ui.product

import com.example.tpkotlin.data.Entities.Product


data class  ProductViewState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null
)