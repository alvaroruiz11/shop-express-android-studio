package com.example.shopexpress.domain.api_client

import com.example.shopexpress.infrastructure.dataclass.CategoriesResponse
import com.example.shopexpress.infrastructure.dataclass.ProductsResponse
import com.example.shopexpress.infrastructure.dataclass.ShopExpressProduct
import com.example.shopexpress.infrastructure.dataclass.UpdateCreateProductResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApiClient {

    @GET("products")
    suspend fun getProducts(): Response<ProductsResponse>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): Response<ShopExpressProduct>

    @GET("products/user-id/{id}")
    suspend fun getProductsByUserId(@Path("id") id: String): Response<ProductsResponse>

    @POST("products")
    suspend fun createProduct(@Body body: PartialCreateProduct) : Response<UpdateCreateProductResponse>

    @PATCH("products/{id}")
    suspend fun updateProduct(@Path("id") id: String, @Body body: PartialProduct) : Response<UpdateCreateProductResponse>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: String) {
    }

    @GET("categories")
    suspend fun getCategories(@Query("limit") limit: String = "20" ): Response<CategoriesResponse>

}

data class PartialProduct(
    val id: String?,
    val title: String?,
    val price: Double?,
    val description: String?,
    val categoryId: String?,
    val image: String?,
)

data class PartialCreateProduct(
    val title: String?,
    val price: Double?,
    val description: String?,
    val categoryId: String?,
    val image: String?,
)