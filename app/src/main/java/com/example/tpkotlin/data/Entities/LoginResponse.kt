package com.example.tpkotlin.data.Entities


data class LoginResponse(
    val message: String,
    val user: LoggedUser,
    val token: String
)

data class LoggedUser(
    val _id: String,
    val username: String,
    val email: String,
    val role: String,
    val createdAt: String,
    val updatedAt: String
)
