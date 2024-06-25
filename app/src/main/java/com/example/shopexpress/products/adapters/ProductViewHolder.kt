package com.example.shopexpress.products.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopexpress.R
import com.example.shopexpress.domain.entities.Product
import com.squareup.picasso.Picasso

class ProductViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val imageProduct = view.findViewById<ImageView>(R.id.imageProduct)
    private val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
    private val tvPrice = view.findViewById<TextView>(R.id.tvPrice)

    fun bind (product: Product ) {
        Picasso.get().load(product.image).into(imageProduct)
        tvTitle.text = product.title
        tvPrice.text = "Bs. ${product.price}"
    }
}