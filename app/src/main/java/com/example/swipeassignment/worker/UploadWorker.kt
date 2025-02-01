package com.example.swipeassignment.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.swipeassignment.data.db.model.LocalProduct
import com.example.swipeassignment.data.db.repository.LocalProductRepository
import com.example.swipeassignment.data.network.repository.ProductRepository
import com.example.swipeassignment.data.network.utils.Resource
import com.example.swipeassignment.utils.isNetworkAvailable
import com.example.swipeassignment.utils.showNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class UploadWorker(appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext, workerParameters), KoinComponent {
    private val productsRepository: ProductRepository by inject()
    private val localProductRepository: LocalProductRepository by inject()
    override fun doWork(): Result {
        val isNetworkAvailable = isNetworkAvailable(applicationContext)
        if (!isNetworkAvailable) {
            return Result.retry()
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val products = localProductRepository.getAllProducts()
                if (products.isNotEmpty()) {
                     products.forEach { localProduct ->
                        val success = addLocalProductToRemoteDb(localProduct)
                        if (!success) {
                            return@launch
                        }
                    }
                }
            }catch (e: Exception){
                Log.e("UploadWorker", "Error: ${e.message}")
                return@launch
            }
        }
        return Result.success()
    }

    private suspend fun addLocalProductToRemoteDb(localProduct: LocalProduct): Boolean {
        val image = localProduct.image?.let {
            val timestamp =
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                    Date()
                )
            val filePath = "$timestamp.jpg"
            val file = File(applicationContext.cacheDir, filePath)
            file.writeBytes(it)
            file
        }
        var uploadSuccess = false
        productsRepository.addProduct(
            productName = localProduct.product_name,
            productType = localProduct.product_type,
            price = localProduct.price.toString(),
            tax = localProduct.tax.toString(),
            file = image
        ).collect {
            when (it) {
                is Resource.Success -> {
                    localProductRepository.deleteProduct(localProduct)
                    uploadSuccess = true
                    showNotification(applicationContext, "Product added!", "Product added successfully")
                }

                is Resource.Error -> {
                    uploadSuccess = false
                }

                is Resource.Loading -> {

                }
            }
        }
        return uploadSuccess

    }
}