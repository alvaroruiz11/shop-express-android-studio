package com.example.shopexpress.config.api

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ShopExpressApi {
    const val BASE_URL = "http://192.168.31.93:3000/api/"

    fun getRetrofit(context: Context?): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun client(context: Context?): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (context != null) {
                addInterceptor(HeaderInterceptor(context))
            }
        }.build()
    }
}