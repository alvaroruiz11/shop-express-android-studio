package com.example.shopexpress.infrastructure.dataclass

data class CategoriesResponse(
    val data: List<ShopExpressCategory>,
    val page: Int,
    val total: Int,
    val totalPage: Int
)

data class ShopExpressCategory(
    val available: Boolean,
    val id: String,
    val name: String
)