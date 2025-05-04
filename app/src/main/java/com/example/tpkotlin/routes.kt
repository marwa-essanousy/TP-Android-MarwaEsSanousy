package com.example.tpkotlin

sealed class routes(val route: String) {
    object Products : routes("products")
    object ProductDetails : routes("product_details")
}