package com.example.swipeassignment.data.db.repository

import com.example.swipeassignment.data.db.model.LocalProduct
import com.example.swipeassignment.data.db.dao.ProductDao

class LocalProductRepository(
    private val productDao: ProductDao
) {
    suspend fun getAllProducts() = productDao.getAllProducts()

    suspend fun insertProduct(product: LocalProduct) = productDao.insertProduct(product)

    suspend fun clearAllProducts() = productDao.clearAllProducts()

    suspend fun deleteProduct(product: LocalProduct) = productDao.deleteProduct(product)
}