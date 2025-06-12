package com.example.tpkotlin.data.Entities

// data/Entities/UserResponse.kt
data class UserResponse(
    val success: Boolean,
    val user: User?,
    val error: String?
)
