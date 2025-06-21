package com.example.tpkotlin.ui.product


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpkotlin.data.Entities.LoginRequest
import com.example.tpkotlin.data.Entities.User
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

class AuthViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    fun register(user: User) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = repository.register(user)
                if (response.isSuccessful) {
                    val message = response.body()?.message ?: "Inscription réussie"
                    _authState.value = AuthState.Success(message)
                } else {
                    // lire le message d’erreur du serveur si disponible
                    val errorMsg = response.errorBody()?.string() ?: "Erreur inconnue"
                    _authState.value = AuthState.Error("Erreur : $errorMsg")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Erreur : ${e.localizedMessage}")
            }
        }
    }

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = repository.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    _authState.value = AuthState.Success(response.body()?.message ?: "Connexion réussie")
                    _isLoggedIn.value = true
                    // Optionnel: stocker token ici
                } else {
                    _authState.value = AuthState.Error(response.errorBody()?.string() ?: "Erreur inconnue")
                    _isLoggedIn.value = false
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Erreur : ${e.localizedMessage}")
                _isLoggedIn.value = false
            }
        }
    }
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}