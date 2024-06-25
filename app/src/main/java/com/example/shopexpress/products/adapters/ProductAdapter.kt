package com.example.shopexpress.products.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopexpress.R
import com.example.shopexpress.domain.entities.Product

class ProductAdapter(private val products: List<Product>, clickListener: ClickListenerProduct): RecyclerView.Adapter<ProductViewHolder>() {
    private val clickListener:ClickListenerProduct = clickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_product, parent, false)
        return ProductViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            clickListener.navigateToProduct(product.id)
        }
    }

    interface ClickListenerProduct {
        fun navigateToProduct(productId: String)
    }
}