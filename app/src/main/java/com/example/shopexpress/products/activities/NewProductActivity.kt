package com.example.shopexpress.products.activities


import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import androidx.lifecycle.lifecycleScope
import com.example.shopexpress.R
import com.example.shopexpress.actions.products.ProductsActions
import com.example.shopexpress.domain.api_client.PartialProduct
import com.example.shopexpress.infrastructure.dataclass.ShopExpressCategory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID


class NewProductActivity : AppCompatActivity() {

    private var categoryId: String = ""
    private var imageUri: Uri? = null
    private var imageProductUrl: String? = null
    private var productId: String? = ""
    private var categories: MutableList<ShopExpressCategory> = mutableListOf()
    private lateinit var storageReference: StorageReference
    private val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        uri?.let {
            imageProductUrl = null
            imageUri = uri
            imageProduct.setImageURI(uri)
            uploadImage()
        }
    }

    //    components
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var fabUploadImage: FloatingActionButton
    private lateinit var imageProduct: ImageView
    private lateinit var inputTitle: TextInputEditText
    private lateinit var inputPrice: TextInputEditText
    private lateinit var inputSelectCategory: AutoCompleteTextView
    private lateinit var inputDescription: TextInputEditText
    private lateinit var btnSave: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_product)
        storageReference = FirebaseStorage.getInstance().reference
        initComponents()
        getCategories {
            initSetInputCategories()
        }
        productId = intent.extras?.getString("productId")
        getProductUpdate(productId)

        fabUploadImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        btnSave.setOnClickListener {
            if (imageProductUrl != null) {
                onSave(it)
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun initComponents() {
        topAppBar = findViewById(R.id.topAppBar)
        topAppBar.navigationIcon?.setTint(
            ContextCompat.getColor(
                this, R.color.md_theme_onBackground
            )
        )
        topAppBar.setNavigationOnClickListener {
            finish()
        }
        fabUploadImage = findViewById(R.id.fabUploadImage)
        imageProduct = findViewById(R.id.imageProduct)
        inputSelectCategory = findViewById(R.id.inputSelectCategory)
        inputTitle = findViewById(R.id.inputTitle)
        inputPrice = findViewById(R.id.inputPrice)
        inputDescription = findViewById(R.id.inputDescription)
        btnSave = findViewById(R.id.btnSave)
    }

    private fun getCategories(function: () -> Unit) {
        val action = ProductsActions(this)
        try {
            lifecycleScope.launch(Dispatchers.IO) {
                val data = action.getCategories()
                categories.clear()
                categories.addAll(data)
                withContext(Dispatchers.Main) {
                    function()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initSetInputCategories() {
        val categoryNames: List<String> = categories.map { category -> category.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categoryNames)
        inputSelectCategory.setAdapter(adapter)

        inputSelectCategory.setOnItemClickListener { parent, _, position, _ ->
            val selectedCategoryName = parent.getItemAtPosition(position) as String
            categoryId = categories.find { it.name == selectedCategoryName }!!.id
        }

    }

    private fun getProductUpdate(productId: String?) {
        if (productId == null) return
        val action = ProductsActions(this)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val product = action.getProductById(productId)
                withContext(Dispatchers.Main) {
                    topAppBar.title = "Actualizar producto"
                    Picasso.get().load(product.image).into(imageProduct)
                    inputTitle.setText(product.title)
                    inputPrice.setText(product.price.toString())
                    inputDescription.setText(product.description)
                    inputSelectCategory.setText(product.category, false)
                    categoryId = product.categoryId!!
                    imageProductUrl = product.image
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage() {
        if (imageUri != null) {
            val fileReference = storageReference.child("uploads/${UUID.randomUUID()}.jpg")
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    fileReference.putFile(imageUri!!).await()
                    val uri = fileReference.downloadUrl.await()
                    imageProductUrl = uri.toString()
                    Log.d("MainActivity", "Download URL: $imageProductUrl")
                } catch (e: Exception) {
                    Log.e("MainActivity", "Image upload failed", e)
                }
            }
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }


    private fun onSave(view: View) {
        val title = inputTitle.text.toString()
        val price = inputPrice.text.toString()
        val description = inputDescription.text.toString()

        if (title.isEmpty() || price.isEmpty() || description.isEmpty() || categoryId.isEmpty()) {
            showSnackbar(view, "Llene todo los campos", "") {}
            return
        }
        val action = ProductsActions(this)
        lifecycleScope.launch(Dispatchers.IO) {
            val product = PartialProduct(
                id = productId,
                title = title,
                price = price.toDouble(),
                description = description,
                categoryId = categoryId,
                image = imageProductUrl
            )
            val resp = action.updateCreateProduct(product)
            if( resp != null ) {
                showSnackbar(view, "Producto Guardado", "Salir") {}
                getProductUpdate(resp.id)
            }
        }
    }

    private fun showSnackbar(
        view: View, message: String, actionText: String?, action: (() -> Unit)?
    ) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        if (actionText != null && action != null) {
            snackbar.setAction(actionText) {
                action()
            }
        }
        snackbar.show()
    }
}