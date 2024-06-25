package com.example.shopexpress.auth.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.shopexpress.R
import com.example.shopexpress.actions.auth.AuthActions
import com.example.shopexpress.config.adapters.DataStoreAdapter
import com.example.shopexpress.products.activities.HomeActivity
import com.example.shopexpress.products.activities.PublicationsActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var tvFullName: TextView
    private lateinit var btnLogout: Button
    private lateinit var topAppBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initBottomNavigation()
        getUser()
        btnLogout = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            onLogout()
        }
    }


    private fun getUser() {
        tvFullName = findViewById(R.id.tvFullName)
        topAppBar = findViewById(R.id.topAppBar)
        val action = AuthActions(this)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val resp = action.checkAuthStatus()
                if( resp != null ) {
                    DataStoreAdapter.setItem(this@ProfileActivity, "token", resp.token)
                    val user = resp.user
                    withContext(Dispatchers.Main) {
                        tvFullName.text = "${user.firstName} ${user.lastName}"
                        topAppBar.subtitle = "${user.firstName} ${user.lastName}"
                    }
                }
            }catch (error: Exception) {
                error.printStackTrace()
            }
        }
    }

    private fun onLogout() {
        lifecycleScope.launch(Dispatchers.IO) {
            DataStoreAdapter.removeItem(this@ProfileActivity, "token")
            withContext(Dispatchers.Main) {
                val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun initBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }

                R.id.page_publications -> {
                    startActivity(Intent(this, PublicationsActivity::class.java))

                    true
                }

                R.id.page_profile -> {

                    true
                }

                else -> {
                    false
                }
            }
        }
        bottomNavigation.selectedItemId = R.id.page_profile
    }
}