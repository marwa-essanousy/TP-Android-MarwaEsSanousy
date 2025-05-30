package com.example.tpkotlin

import com.example.tpkotlin.data.Entities.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ProductApi {
    @GET("products.json")
    suspend fun getProducts(): List<Product>
}
object RetrofitInstance {
    private const val BASE_URL = "https://raw.githubusercontent.com/marwa-essanousy/Api_Ecomm/refs/heads/retrofit/app/public/products-api/"
    val api: ProductApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApi::class.java)
    }
}
