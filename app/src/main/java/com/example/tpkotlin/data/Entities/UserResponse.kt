package com.example.tpkotlin.data.Entities

// data/Entities/UserResponse.kt
data class UserResponse(
    val success: Boolean,
    val user: User?,       // peut être null en cas d'erreur
    val error: String?     // message d'erreur s'il y en a un
)
