package com.example.shopexpress.domain.entities

data class Product(
    val id: String,
    val title: String,
    val price: Double,
    val available: Boolean,
    val image: String,
    val description: String?,
    val categoryId: String?,
    val category: String?,
    val userId: String?,
    val fullNameUser: String?,
    val createdAt: String?,
    val updatedAt: String?,
)
