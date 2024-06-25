package com.example.shopexpress.products.adapters.publication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopexpress.R
import com.example.shopexpress.domain.entities.Product

class ProductPublicationAdapter(private val products: List<Product>, clickListenerPublication: ClickListenerPublication): RecyclerView.Adapter<ProductPublicationViewHolder>() {
    private val clickListener: ClickListenerPublication = clickListenerPublication
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductPublicationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductPublicationViewHolder(layoutInflater, clickListener)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductPublicationViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            clickListener.navigateToUpdateProduct(product.id)
        }
    }

    interface ClickListenerPublication {
        fun navigateToUpdateProduct(productId: String)
        fun onDeleteProduct(productId: String)
    }
}