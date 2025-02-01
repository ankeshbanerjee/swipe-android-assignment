package com.example.swipeassignment.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.swipeassignment.data.db.dao.ProductDao
import com.example.swipeassignment.data.db.model.LocalProduct

@Database(entities = [LocalProduct::class], version = 1, exportSchema = false)
abstract class ProductDb : RoomDatabase() {
    abstract fun productDao(): ProductDao
}