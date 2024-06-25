package com.example.shopexpress.actions.products

import android.content.Context
import com.example.shopexpress.config.api.ShopExpressApi
import com.example.shopexpress.domain.api_client.PartialCreateProduct
import com.example.shopexpress.domain.api_client.PartialProduct
import com.example.shopexpress.domain.api_client.ProductsApiClient
import com.example.shopexpress.domain.entities.Product
import com.example.shopexpress.infrastructure.dataclass.ShopExpressCategory
import com.example.shopexpress.infrastructure.dataclass.UpdateCreateProductResponse
import com.example.shopexpress.infrastructure.mappers.ProductsMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ProductsActions(context: Context) {
    private val retrofit = ShopExpressApi.getRetrofit(context).create(ProductsApiClient::class.java)
    suspend fun getProducts(): List<Product> {
        return withContext(Dispatchers.IO) {
            val resp = retrofit.getProducts()
            val data = resp.body()?.data ?: emptyList()
            val products: List<Product> =
                data.map { it -> ProductsMapper.shopExpressProductToEntity(it) }
            products
        }
    }

    suspend fun getProductsByUserId(userId: String): List<Product> {
        return withContext(Dispatchers.IO) {
            val resp = retrofit.getProductsByUserId(userId)
            val data = resp.body()?.data ?: emptyList()
            val products: List<Product> =
                data.map { it -> ProductsMapper.shopExpressProductToEntity(it) }
            products
        }
    }

    suspend fun getProductById(id: String): Product {
        return withContext(Dispatchers.IO) {
            val resp = retrofit.getProductById(id)
            val product = resp.body()!!
            ProductsMapper.shopExpressProductToEntity(product)
        }
    }

    suspend fun getCategories(): List<ShopExpressCategory> {
        return withContext(Dispatchers.IO) {
            val resp = retrofit.getCategories()
            val categories = resp.body()?.data ?: emptyList()
            categories
        }
    }

    suspend fun deleteProduct(productId: String): Boolean {
        return try {
            retrofit.deleteProduct(productId)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } catch (e: HttpException) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateCreateProduct ( product: PartialProduct ): UpdateCreateProductResponse? {
        if ( product.id != null ) {
            return updateProduct(product)
        } else {
            return createProduct(product)
        }
    }

    private suspend fun updateProduct( body: PartialProduct): UpdateCreateProductResponse? {
        return try {
            val data = retrofit.updateProduct(body.id!!, body)
            data.body()
        } catch ( e: Exception ) {
            e.printStackTrace()
            null
        } catch ( e: HttpException ) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun createProduct( body: PartialProduct): UpdateCreateProductResponse? {
        return try {
            val product = PartialCreateProduct(
                title = body.title,
                price = body.price,
                description = body.description,
                categoryId = body.categoryId,
                image = body.image
            )
            val data = retrofit.createProduct(product)
            data.body()
        } catch ( e: Exception ) {
            e.printStackTrace()
            null
        } catch ( e: HttpException ) {
            e.printStackTrace()
            null
        }
    }

}
