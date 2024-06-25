package com.example.shopexpress.actions.auth

import android.content.Context
import com.example.shopexpress.config.api.ShopExpressApi
import com.example.shopexpress.domain.api_client.AuthApiClient
import com.example.shopexpress.domain.api_client.LoginBody
import com.example.shopexpress.domain.api_client.RegisterBody
import com.example.shopexpress.infrastructure.dataclass.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthActions(context: Context) {
    private val retrofit = ShopExpressApi.getRetrofit(context).create(AuthApiClient::class.java)

    suspend fun login (body: LoginBody): AuthResponse? {
        return withContext(Dispatchers.IO) {
            val resp = retrofit.login(body)
            resp.body() ?: null
        }
    }

    suspend fun register(body: RegisterBody): AuthResponse? {
        return withContext(Dispatchers.IO){
            val resp = retrofit.register(body)
            resp.body() ?: null
        }
    }

    suspend fun checkAuthStatus(): AuthResponse? {
        return withContext(Dispatchers.IO) {
            val resp = retrofit.checkAuthStatus()
            resp.body() ?: null
        }
    }
}