package com.example.swipeassignment.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.swipeassignment.data.db.model.LocalProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM product_table")
    suspend fun getAllProducts() : List<LocalProduct>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: LocalProduct)

    @Query("DELETE FROM product_table")
    suspend fun clearAllProducts()

    @Delete
    suspend fun deleteProduct(product: LocalProduct)
}