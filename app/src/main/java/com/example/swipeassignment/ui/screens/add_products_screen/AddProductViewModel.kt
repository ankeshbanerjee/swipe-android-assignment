package com.example.swipeassignment.ui.screens.add_products_screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.swipeassignment.data.db.model.LocalProduct
import com.example.swipeassignment.data.db.repository.LocalProductRepository
import com.example.swipeassignment.data.network.utils.Resource
import com.example.swipeassignment.data.network.repository.ProductRepository
import com.example.swipeassignment.utils.UiState
import com.example.swipeassignment.utils.isNetworkAvailable
import com.example.swipeassignment.utils.showNotification
import com.example.swipeassignment.utils.showToast
import com.example.swipeassignment.worker.UploadWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class AddProductViewModel(
    private val appContext: Context,
    private val productRepository: ProductRepository,
    private val localProductRepository: LocalProductRepository,
) : ViewModel() {

    val productsList = listOf("Product", "Service", "Tax")
    private val _isDropDownExpanded = MutableStateFlow(false)
    val isDropDownExpanded = _isDropDownExpanded.asStateFlow()
    private val _selectedProduct = MutableStateFlow(productsList[0])
    val selectedProduct = _selectedProduct.asStateFlow()

    fun toggleDropDown() {
        _isDropDownExpanded.value = !_isDropDownExpanded.value
    }

    fun setSelectedProduct(product: String) {
        _selectedProduct.value = product
    }

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _sellingPrice = MutableStateFlow("0.0")
    val sellingPrice = _sellingPrice.asStateFlow()

    private val _taxRate = MutableStateFlow("0.0")
    val taxRate = _taxRate.asStateFlow()

    private val _image = MutableStateFlow<File?>(null)
    val image = _image.asStateFlow()

    private val _uiState = MutableStateFlow(UiState.IDLE)
    val uiState = _uiState.asStateFlow()

    fun setImage(file: File?) {
        _image.value = file
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setSellingPrice(sellingPrice: String) {
        _sellingPrice.value = sellingPrice
    }

    fun setTaxRate(taxRate: String) {
        _taxRate.value = taxRate
    }


    fun handleAddProduct(
        goBack: () -> Unit
    ) {
        val name = _name.value
        val productType = _selectedProduct.value
        val price = _sellingPrice.value.toDouble().toString()
        val tax = _taxRate.value.toDouble().toString()
        val file = _image.value
        if (name.trim()
                .isEmpty() || price.toDouble() == "0.0".toDouble() || tax.toDouble() == "0.0".toDouble()
        ) {
            showToast(appContext, "Please fill all the fields")
            return
        }
        viewModelScope.launch {
            if (!isNetworkAvailable(appContext)) {
                localProductRepository.insertProduct(
                    LocalProduct(
                        product_name = name,
                        product_type = productType,
                        tax = tax.toDouble(),
                        image = file?.readBytes(),
                        price = price.toDouble()
                    )
                )
                scheduleProductSync(appContext)
                showToast(appContext, "Product added locally")
                goBack()
            } else {
                productRepository.addProduct(name, productType, price, tax, file)
                    .collect { response ->
                        when (response) {
                            is Resource.Success -> {
                                _uiState.value = UiState.SUCCESS
                                showToast(appContext, "Product added successfully")
                                showNotification(appContext, "Product added!", "Product added successfully")
                                goBack()
                            }

                            is Resource.Error -> {
                                response.message?.let {
                                    Log.e("ADD_PRODUCT_ERROR", it)
                                    showToast(appContext, it)
                                }
                                _uiState.update { UiState.ERROR }
                            }

                            is Resource.Loading -> {
                                _uiState.update { UiState.LOADING }
                            }
                        }
                    }
            }

        }
    }

    private fun scheduleProductSync(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

}