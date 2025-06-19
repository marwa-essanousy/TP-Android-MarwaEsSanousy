package com.example.tpkotlin.data.Entities

// data/Entities/UserResponse.kt
data class UserResponse(
    val message: String,
    val user: UserData? = null
)

data class UserData(
    val id: Int,
    val username: String,
    val email: String
)
