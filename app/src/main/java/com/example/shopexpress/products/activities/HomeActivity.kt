package com.example.shopexpress.products.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.shopexpress.R
import com.example.shopexpress.actions.products.ProductsActions
import com.example.shopexpress.auth.activities.ProfileActivity
import com.example.shopexpress.domain.entities.Product
import com.example.shopexpress.products.adapters.ProductAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(), ProductAdapter.ClickListenerProduct {

    private lateinit var recycleViewProduct: RecyclerView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initBottomNavigation()
        getProducts()

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            getProducts()
            swipeRefreshLayout.isRefreshing = false
        }

    }

    private fun getProducts() {
        val action = ProductsActions(this)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val products = action.getProducts()
                runOnUiThread {
                    initRecycleViewProducts(products)
                }
            } catch (error: Exception) {
                error.printStackTrace()
            }

        }
    }

    private fun initRecycleViewProducts(products: List<Product>) {
        recycleViewProduct = findViewById(R.id.recycleViewProduct)
        recycleViewProduct.layoutManager = GridLayoutManager(this, 2)
        val adapter = ProductAdapter(products, this)
        recycleViewProduct.adapter = adapter
    }


    override fun navigateToProduct(productId: String) {
        val intent = Intent(this, ProductActivity::class.java)
        intent.putExtra("productId", productId)
        startActivity(intent)
    }

    private fun initBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_home -> {
                    true
                }

                R.id.page_publications -> {
                    startActivity(Intent(this, PublicationsActivity::class.java))

                    true
                }

                R.id.page_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))

                    true
                }

                else -> {
                    false
                }
            }
        }
        bottomNavigation.selectedItemId = R.id.page_home
    }
}