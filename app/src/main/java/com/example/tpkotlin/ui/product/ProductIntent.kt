package com.example.tpkotlin.ui.product



sealed class ProductIntent {
    object LoadProducts : ProductIntent()
}