package com.example.shopexpress.domain.api_client

import com.example.shopexpress.infrastructure.dataclass.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiClient {
    @GET("auth/check-status")
    suspend fun checkAuthStatus(): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body body: LoginBody): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body body: RegisterBody): Response<AuthResponse>

}

data class LoginBody(
    val email: String,
    val password: String
)

data class RegisterBody(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String)