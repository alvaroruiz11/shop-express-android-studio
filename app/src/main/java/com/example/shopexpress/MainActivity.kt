package com.example.shopexpress

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.shopexpress.actions.auth.AuthActions
import com.example.shopexpress.auth.activities.LoginActivity
import com.example.shopexpress.config.adapters.DataStoreAdapter
import com.example.shopexpress.products.activities.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onCheckAuthStatus()
    }

    private fun onCheckAuthStatus() {
        val action = AuthActions(this)
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val token = DataStoreAdapter.getItem(this@MainActivity, "token")
                if (token != "") {
                    val data = action.checkAuthStatus()
                    if (data != null) {
                        DataStoreAdapter.setItem(this@MainActivity, "token", data.token)
                        toHome()
                    } else {
                        DataStoreAdapter.removeItem(this@MainActivity, "token")
                        toLogin()
                    }
                }else {
                    DataStoreAdapter.removeItem(this@MainActivity, "token")
                    toLogin()
                }
            } catch (error: Exception) {
                error.printStackTrace()
            }
        }
    }

    private fun toHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun toLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}