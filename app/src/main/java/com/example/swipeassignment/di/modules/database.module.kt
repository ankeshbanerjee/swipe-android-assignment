package com.example.swipeassignment.di.modules

import androidx.room.Room
import com.example.swipeassignment.data.db.database.ProductDb
import com.example.swipeassignment.data.db.repository.LocalProductRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), ProductDb::class.java, "product_database").build()
    }
    single {
        get<ProductDb>().productDao()
    }
    single {
        LocalProductRepository(get())
    }
}