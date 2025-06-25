package com.example.tpkotlin.ui.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpkotlin.data.Entities.LoginRequest
import com.example.tpkotlin.data.Entities.User
import com.example.tpkotlin.data.Repository.SharedPreferencesManager
import com.example.tpkotlin.data.Repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val error: String) : AuthState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository()
    private val sharedPreferencesManager = SharedPreferencesManager(application)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(sharedPreferencesManager.isLoggedIn())
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _username = MutableStateFlow(sharedPreferencesManager.getUsername() ?: "")
    val username = _username.asStateFlow()

    var onUsernameUpdated: ((String) -> Unit)? = null

    init {
        _username.value = sharedPreferencesManager.getUsername() ?: ""
    }

    fun register(user: User) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = repository.register(user)
                if (response.isSuccessful) {
                    val message = response.body()?.message ?: "Registration successful"
                    _authState.value = AuthState.Success(message)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    _authState.value = AuthState.Error("Error : $errorMsg")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Error : ${e.localizedMessage}")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = repository.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    _authState.value = AuthState.Success(response.body()?.message ?: "Connection successful")
                    _isLoggedIn.value = true

                    sharedPreferencesManager.saveUsername(email)
                    sharedPreferencesManager.saveLoginState(true)
                    _username.value = email

                    onUsernameUpdated?.invoke(email)

                } else {
                    _authState.value = AuthState.Error(response.errorBody()?.string() ?: "Unknown error")
                    _isLoggedIn.value = false
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Error : ${e.localizedMessage}")
                _isLoggedIn.value = false
            }
        }
    }

    fun logout() {
        _isLoggedIn.value = false
        _username.value = ""
        sharedPreferencesManager.clearUserData()
        _authState.value = AuthState.Idle

        onUsernameUpdated?.invoke("Guest")
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}