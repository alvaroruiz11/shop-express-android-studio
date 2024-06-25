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
import com.example.shopexpress.domain.api_client.RegisterBody
import com.example.shopexpress.products.activities.HomeActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var inputFirstName: TextInputEditText
    private lateinit var inputLastName: TextInputEditText
    private lateinit var inputEmail: TextInputEditText
    private lateinit var inputPassword: TextInputEditText
    private lateinit var btnRegister: Button
    private lateinit var tvToLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        inputFirstName = findViewById(R.id.inputFirstName)
        inputLastName = findViewById(R.id.inputLastName)
        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvToLogin = findViewById(R.id.tvToLogin)

        btnRegister.setOnClickListener {
            onRegister()
        }

        tvToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onRegister() {
        val firstName = inputFirstName.text.toString()
        val lastName = inputLastName.text.toString()
        val email = inputEmail.text.toString()
        val password = inputPassword.text.toString()
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("Campos requeridos")
            return
        }
        val action = AuthActions(this)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val data = action.register(RegisterBody(firstName, lastName, email, password))
                if( data != null ) {
                    DataStoreAdapter.setItem(this@RegisterActivity, "token", data.token)
                    navigateToHome()
                }
            } catch (error: Exception) {
                error.printStackTrace()
            }
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}