package com.example.swipeassignment.data.repository

import com.example.swipeassignment.data.network.service.ProductService
import com.example.swipeassignment.data.network.utils.safeApiCall
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProductRepository(
    private val productService: ProductService
) {
    fun getProducts() = safeApiCall {
        productService.getProducts()
    }

    fun addProduct(
        productName: String,
        productType: String,
        price: String,
        tax: String,
        file: File? = null
    ) = safeApiCall {
        productService.addProduct(
            productName = productName.toRequestBody("text/plain".toMediaTypeOrNull()),
            productType = productType.toRequestBody("text/plain".toMediaTypeOrNull()),
            price = price.toRequestBody("text/plain".toMediaTypeOrNull()),
            tax = tax.toRequestBody("text/plain".toMediaTypeOrNull()),
            files = file?.let {
                listOf(
                    MultipartBody.Part.createFormData(
                        name = "files[]",
                        filename = it.name,
                        body = it.asRequestBody(
                            "image/*".toMediaTypeOrNull()
                        )
                    )
                )
            }
        )
    }

}