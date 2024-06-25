package com.example.shopexpress.infrastructure.dataclass

data class UpdateCreateProductResponse(
    val available: Boolean,
    val categoryId: String,
    val createdAt: String,
    val description: String,
    val id: String,
    val image: String,
    val price: Double,
    val title: String,
    val updatedAt: String,
    val userId: String
)