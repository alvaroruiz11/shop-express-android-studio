package com.example.shopexpress.infrastructure.dataclass

data class ProductsResponse(
    val `data`: List<ShopExpressProduct>,
    val page: Int,
    val total: Int,
    val totalPage: Int
)

data class ShopExpressProduct(
    val Category: Category?,
    val User: UserProduct?,
    val available: Boolean,
    val categoryId: String?,
    val createdAt: String?,
    val description: String,
    val id: String,
    val image: String,
    val price: Double,
    val title: String,
    val updatedAt: String?,
    val userId: String?
)

data class Category(
    val name: String
)

data class UserProduct(
    val firstName: String,
    val img: String?,
    val lastName: String
)