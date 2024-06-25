package com.example.shopexpress.products.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.shopexpress.R
import com.example.shopexpress.actions.products.ProductsActions
import com.google.android.material.appbar.MaterialToolbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductActivity : AppCompatActivity() {
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var imageProduct: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvFullNameUser: TextView
    private lateinit var tvCategory: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        initComponent()
        val productId = intent.extras?.getString("productId") ?: ""
        getProductById(productId)

        topAppBar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.md_theme_onBackground))
        topAppBar.setNavigationOnClickListener {
            finish()
        }
    }


    private fun initComponent() {
        topAppBar = findViewById(R.id.topAppBar)
        imageProduct = findViewById(R.id.imageProduct)
        tvTitle = findViewById(R.id.tvTitle)
        tvPrice = findViewById(R.id.tvPrice)
        tvDescription = findViewById(R.id.tvDescription)
        tvFullNameUser = findViewById(R.id.tvFullNameUser)
        tvCategory = findViewById(R.id.tvCategory)
    }

    private fun getProductById(id: String) {
        if (id == "") {
            finish()
        }
        val action = ProductsActions(this)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val product = action.getProductById(id)
                runOnUiThread {
                    Picasso.get().load(product.image).into(imageProduct)
                    tvTitle.text = product.title
                    tvPrice.text = "Bs. ${product.price}"
                    tvDescription.text = product.description
                    tvFullNameUser.text = product.fullNameUser
                    tvCategory.text = product.category
                }

            } catch (error: Exception) {
                error.printStackTrace()
            }
        }
    }


}