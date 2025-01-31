package com.example.swipeassignment.di.modules

import com.example.swipeassignment.data.network.service.ProductService
import com.example.swipeassignment.data.repository.ProductRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        Retrofit
            .Builder()
            .baseUrl("https://app.getswipe.in/api/public/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<ProductService> {
        get<Retrofit>().create(ProductService::class.java)
    }
    single {
        ProductRepository(get())
    }
}
