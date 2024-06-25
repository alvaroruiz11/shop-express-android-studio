package com.example.shopexpress.infrastructure.dataclass

data class AuthResponse(
    val token: String,
    val user: User
)

data class User(
    val email: String,
    val firstName: String,
    val id: String,
    val img: Any,
    val isActive: Boolean,
    val lastName: String,
    val roles: List<String>
)
