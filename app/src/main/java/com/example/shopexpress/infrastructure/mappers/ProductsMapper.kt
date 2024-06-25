package com.example.shopexpress.infrastructure.mappers

import com.example.shopexpress.config.api.ShopExpressApi
import com.example.shopexpress.domain.entities.Product
import com.example.shopexpress.infrastructure.dataclass.ShopExpressProduct

class ProductsMapper {
    companion object {
        fun shopExpressProductToEntity(data: ShopExpressProduct): Product {
            return Product(
                data.id,
                data.title,
                data.price,
                data.available,
                data.image,
                data.description,
                data.categoryId ?: null,
                data.Category?.name ?: null,
                data.userId ?: null,
                if (data.User != null) {
                    "${data.User.firstName} ${data.User.lastName}"
                } else {
                    null
                },
                data.createdAt ?: null,
                data.updatedAt ?: null
            )
        }
    }
}