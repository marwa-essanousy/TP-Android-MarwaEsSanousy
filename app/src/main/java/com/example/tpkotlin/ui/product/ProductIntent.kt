package com.example.tpkotlin.ui.product


// Intent
sealed class ProductIntent {
    object LoadProducts : ProductIntent()
}