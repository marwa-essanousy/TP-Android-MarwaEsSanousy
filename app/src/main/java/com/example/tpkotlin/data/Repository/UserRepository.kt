package com.example.tpkotlin.data.Repository

// data/Repository/UserRepository.kt
import com.example.tpkotlin.data.Entities.*
import retrofit2.Response

class UserRepository {
    suspend fun register(user: User): Response<UserResponse> {
        return RetrofitInstance.api.register(user)
    }

    suspend fun login(email: String, password: String): Response<UserResponse> {
        return RetrofitInstance.api.login(LoginRequest(email, password))
    }
}
