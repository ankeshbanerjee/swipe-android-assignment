package com.example.swipeassignment.ui.screens.products_screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipeassignment.data.network.model.product.Product
import com.example.swipeassignment.data.network.utils.Resource
import com.example.swipeassignment.data.repository.ProductRepository
import com.example.swipeassignment.utils.UiState
import com.example.swipeassignment.utils.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductsViewModel (
    private val appContext: Context,
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    private val _uiState = MutableStateFlow(UiState.IDLE)
    val uiState = _uiState.asStateFlow()

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            productRepository.getProducts().collect { response ->
                when(response) {
                    is Resource.Success -> {
                        response.data.also { res ->
                            val responseBody = res.body()
                            if (responseBody == null) {
                                Log.e("GET_PRODUCTS_ERROR", "Response body is null")
                                return@collect
                            }
                            _products.value = responseBody
                            _uiState.value = UiState.SUCCESS
                        }
                    }
                    is Resource.Error -> {
                        response.message?.let {
                            Log.e("GET_PRODUCTS_ERROR", it)
                            showToast(appContext, it)
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.value = UiState.LOADING
                    }
                }
            }
        }
    }
}