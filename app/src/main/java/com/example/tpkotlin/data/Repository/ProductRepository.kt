package com.example.tpkotlin.data.Repository



import com.example.tpkotlin.R
import com.example.tpkotlin.RetrofitInstance
import com.example.tpkotlin.data.Entities.Product
import kotlinx.coroutines.delay
import javax.inject.Inject

class ProductRepository @Inject constructor() {
    suspend fun getProducts(): List<Product> {
        return RetrofitInstance.api.getProducts()
    }
}
