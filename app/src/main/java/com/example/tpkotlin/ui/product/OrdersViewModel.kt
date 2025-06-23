package com.example.tpkotlin.ui.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.tpkotlin.data.Entities.Order
import com.example.tpkotlin.data.Repository.SharedPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrdersViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPrefsManager = SharedPreferencesManager(application.applicationContext)

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    init {
        loadOrders()
    }

    fun loadOrders() {
        _orders.value = sharedPrefsManager.getOrders()
    }
}
