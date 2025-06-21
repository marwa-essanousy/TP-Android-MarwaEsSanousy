package com.example.tpkotlin.data.Repository


import android.content.Context
import android.content.SharedPreferences
import com.example.tpkotlin.data.Entities.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "UserPrefs",
        Context.MODE_PRIVATE
    )
    private val gson = Gson()

    companion object {
        private const val KEY_USERNAME = "username"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_CART_ITEMS = "cart_items"
        private const val KEY_FAVORITE_PRODUCT_IDS = "favorite_product_ids"
    }

    fun saveUsername(username: String) {
        sharedPreferences.edit().putString(KEY_USERNAME, username).apply()
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(KEY_USERNAME, null)
    }

    fun saveLoginState(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun saveCartItems(cartItems: List<Product>) {
        val cartItemsJson = gson.toJson(cartItems)
        sharedPreferences.edit().putString(KEY_CART_ITEMS, cartItemsJson).apply()
    }

    fun getCartItems(): List<Product> {
        val cartItemsJson = sharedPreferences.getString(KEY_CART_ITEMS, "[]")
        val type = object : TypeToken<List<Product>>() {}.type
        return try {
            gson.fromJson(cartItemsJson, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveFavoriteProductIds(favoriteProductIds: Set<Int>) {
        val favoriteIdsJson = gson.toJson(favoriteProductIds.toList())
        sharedPreferences.edit().putString(KEY_FAVORITE_PRODUCT_IDS, favoriteIdsJson).apply()
    }

    fun getFavoriteProductIds(): Set<Int> {
        val favoriteIdsJson = sharedPreferences.getString(KEY_FAVORITE_PRODUCT_IDS, "[]")
        val type = object : TypeToken<List<Int>>() {}.type
        return try {
            val list = gson.fromJson<List<Int>>(favoriteIdsJson, type) ?: emptyList()
            list.toSet()
        } catch (e: Exception) {
            emptySet()
        }
    }

    fun clearUserData() {
        sharedPreferences.edit().clear().apply()
    }

    fun clearCart() {
        sharedPreferences.edit().remove(KEY_CART_ITEMS).apply()
    }

    fun clearFavorites() {
        sharedPreferences.edit().remove(KEY_FAVORITE_PRODUCT_IDS).apply()
    }
}