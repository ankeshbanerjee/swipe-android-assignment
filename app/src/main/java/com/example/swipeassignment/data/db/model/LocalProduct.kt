package com.example.swipeassignment.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class LocalProduct(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "product_name") var product_name: String,
    @ColumnInfo(name = "product_type") var product_type: String,
    @ColumnInfo(name = "price") var price: Double,
    @ColumnInfo(name = "tax") var tax: Double,
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB) var image: ByteArray? = null
)
