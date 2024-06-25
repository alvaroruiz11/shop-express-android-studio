package com.example.shopexpress.auth.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.shopexpress.R
import com.example.shopexpress.actions.auth.AuthActions
import com.example.shopexpress.config.adapters.DataStoreAdapter

import com.example.shopexpress.domain.api_client.LoginBody
import com.example.shopexpress.products.activities.HomeActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var inputEmail: TextInputEditText
    private lateinit var inputPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var tvToRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvToRegister = findViewById(R.id.tvToRegister)

        btnLogin.setOnClickListener {
            onLogin()
        }

        tvToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }


    private fun onLogin() {
        val email: String = inputEmail.text.toString()
        val password: String = inputPassword.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            showError("Campos requeridos")
            return
        }
        val action = AuthActions(this)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val data = action.login(LoginBody(email, password))
                if (data != null) {
                    DataStoreAdapter.setItem(this@LoginActivity, "token", data.token)
                    navigateToHome()
                }
            } catch (error: Exception) {
                error.printStackTrace()
            }
        }

    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}