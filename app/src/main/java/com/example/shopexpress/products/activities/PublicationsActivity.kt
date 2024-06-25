package com.example.shopexpress.products.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.shopexpress.R
import com.example.shopexpress.actions.auth.AuthActions
import com.example.shopexpress.actions.products.ProductsActions
import com.example.shopexpress.auth.activities.ProfileActivity
import com.example.shopexpress.config.adapters.DataStoreAdapter
import com.example.shopexpress.domain.entities.Product
import com.example.shopexpress.products.adapters.publication.ProductPublicationAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PublicationsActivity : AppCompatActivity(), ProductPublicationAdapter.ClickListenerPublication {
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var rvProductsByUser: RecyclerView
    private lateinit var fabNewProduct: ExtendedFloatingActionButton
    private lateinit var rootView: View
    private var isLoading: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publications)
        initBottomNavigation()
        navigateNewProduct()
        geProductsByUserId()
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            geProductsByUserId()
            swipeRefreshLayout.isRefreshing = false
        }

        rootView = findViewById(android.R.id.content)


    }


    private fun geProductsByUserId(){
        val action = ProductsActions(this)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                isLoading = true
                val userId = getUserById()
                if (userId=="") return@launch
                val products = action.getProductsByUserId(userId)
                withContext(Dispatchers.Main) {
                    initRecycleViewProductsByUser(products)
                    isLoading = false
                }
            } catch (error: Exception) {
                error.printStackTrace()
            }
        }

    }

    private suspend fun getUserById(): String {
        val action = AuthActions(this)
        return withContext(Dispatchers.IO) {
            val resp = action.checkAuthStatus()
            if( resp != null ) {
                DataStoreAdapter.setItem(this@PublicationsActivity, "token", resp.token)
                resp.user.id
            }else {
                ""
            }
        }
    }

    private fun initRecycleViewProductsByUser(products: List<Product>) {
        rvProductsByUser = findViewById(R.id.rvProductsByUser)
        rvProductsByUser.layoutManager = LinearLayoutManager(this)
        val adapter = ProductPublicationAdapter(products, this)
        rvProductsByUser.adapter = adapter
    }

    private fun navigateNewProduct() {
        fabNewProduct = findViewById(R.id.fabNewProduct)
        fabNewProduct.setOnClickListener {
            startActivity(Intent(this, NewProductActivity::class.java))
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
        bottomNavigation.selectedItemId = R.id.page_publications
    }

    override fun navigateToUpdateProduct(productId: String) {
        val intent = Intent(this, NewProductActivity::class.java)
        intent.putExtra("productId", productId)
        startActivity(intent)
    }

    override fun onDeleteProduct(productId: String) {
        Snackbar.make(rootView, "¿Estás seguro de borrar este producto?", Snackbar.LENGTH_LONG)
            .setAction("Sí") {
                val action = ProductsActions(this)
                try {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val resp = action.deleteProduct(productId)
                        if( resp ) {
                            geProductsByUserId()
                            return@launch
                        }
                    }
                } catch (e: Exception ) {
                    e.printStackTrace()
                }
            }.setBackgroundTint(resources.getColor(R.color.md_theme_errorContainer))
            .setActionTextColor(resources.getColor(R.color.md_theme_error))
            .setTextColor(resources.getColor(R.color.md_theme_error))
            .show()

    }
}