package com.example.shopexpress.config.api

import android.content.Context
import com.example.shopexpress.config.adapters.DataStoreAdapter
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${getToken(context)}")
            .build()

        return chain.proceed(request)
    }

    private fun getToken(context: Context): String {
        return runBlocking { DataStoreAdapter.getItem(context, "token") }
    }
}