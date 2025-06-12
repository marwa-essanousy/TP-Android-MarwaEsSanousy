package com.example.tpkotlin.data.Repository

import com.example.tpkotlin.data.Entities.LoginRequest
import com.example.tpkotlin.data.Entities.User
import com.example.tpkotlin.data.Entities.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("api/signup")
    suspend fun register(@Body user: User): Response<UserResponse>

    @POST("api/signin")
    suspend fun login(@Body loginRequest: LoginRequest): Response<UserResponse>
}
