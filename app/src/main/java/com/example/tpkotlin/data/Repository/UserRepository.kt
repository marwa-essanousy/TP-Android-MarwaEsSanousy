package com.example.tpkotlin.data.Repository

import com.example.tpkotlin.data.Entities.*
import retrofit2.Response

class UserRepository {
    suspend fun register(user: User): Response<UserResponse> {
        return RetrofitInstance.api.register(user)
    }

    suspend fun login(request: LoginRequest): Response<LoginResponse> {
        return RetrofitInstance.api.login(request)
    }
}