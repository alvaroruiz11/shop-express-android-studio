package com.example.shopexpress.products.adapters.publication

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopexpress.R
import com.example.shopexpress.domain.entities.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso


class ProductPublicationViewHolder(view: View, clickListenerPublication:ProductPublicationAdapter.ClickListenerPublication): RecyclerView.ViewHolder(view) {

    private val clickListener: ProductPublicationAdapter.ClickListenerPublication = clickListenerPublication
    private val ivProduct = view.findViewById<ImageView>(R.id.ivProduct)
    private val txtTitle = view.findViewById<TextView>(R.id.txtTitle)
    private val txtPrice = view.findViewById<TextView>(R.id.txtPrice)
    private val fabDeleteProduct = view.findViewById<FloatingActionButton>(R.id.fabDeleteProduct)
    fun bind (product: Product) {
        Picasso.get().load(product.image).into(ivProduct)
        txtTitle.text = product.title
        txtPrice.text = "Bs. ${product.price}"
        fabDeleteProduct.setOnClickListener {
            clickListener.onDeleteProduct(product.id)
        }
    }
}