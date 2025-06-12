package com.example.tpkotlin.ui.product


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                if (response.isSuccessful && response.body()?.success == true) {
                    _authState.value = AuthState.Success("Inscription réussie")
                } else {
                    _authState.value = AuthState.Error(response.body()?.error ?: "Erreur inconnue")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Erreur : ${e.localizedMessage}")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = repository.login(email, password)
                if (response.isSuccessful && response.body()?.success == true) {
                    _authState.value = AuthState.Success("Connexion réussie")
                } else {
                    _authState.value = AuthState.Error(response.body()?.error ?: "Email ou mot de passe incorrect")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Erreur : ${e.localizedMessage}")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
