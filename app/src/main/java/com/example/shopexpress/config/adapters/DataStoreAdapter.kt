package com.example.shopexpress.config.adapters

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "SHOPEXPRESS_PREFERENCE")
object DataStoreAdapter {

    suspend fun setItem (context: Context, key: String, value: String) {
        context.dataStore.edit { preference ->
            preference[stringPreferencesKey(key)] = value
        }
    }
    suspend fun getItem (context: Context, key: String): String {
        val wrapperKey = stringPreferencesKey(key)
        val valueFlow: Flow<String> = context.dataStore.data.map {
            it[wrapperKey] ?: ""
        }
        return valueFlow.first()
    }

    suspend fun removeItem (context: Context, key: String) = context.dataStore.edit { preference ->
        preference.remove(stringPreferencesKey(key))
    }
}