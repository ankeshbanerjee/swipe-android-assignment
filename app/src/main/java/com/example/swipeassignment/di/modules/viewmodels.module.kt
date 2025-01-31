package com.example.swipeassignment.di.modules

import com.example.swipeassignment.ui.screens.add_products_screen.AddProductViewModel
import com.example.swipeassignment.ui.screens.products_screen.ProductsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val viewModelsModule = module {
    viewModel {
        ProductsViewModel(androidContext(), get())
    }
    viewModel {
        AddProductViewModel(androidContext(),get())
    }
}