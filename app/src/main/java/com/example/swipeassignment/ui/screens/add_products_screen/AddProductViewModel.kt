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

/**
 * ViewModel responsible for managing product addition logic.
 * It handles user input, local storage, and network operations.
 *
 * @param appContext The application context for accessing resources.
 * @param productRepository Repository handling remote product storage.
 * @param localProductRepository Repository handling local product storage when offline.
 */
class AddProductViewModel(
    private val appContext: Context,
    private val productRepository: ProductRepository,
    private val localProductRepository: LocalProductRepository,
) : ViewModel() {

    /** List of product types available for selection. */
    val productsList = listOf("Product", "Service", "Tax")

    /** state for dropdown expansion. */
    private val _isDropDownExpanded = MutableStateFlow(false)
    val isDropDownExpanded = _isDropDownExpanded.asStateFlow()

    /** state for selected product type. */
    private val _selectedProduct = MutableStateFlow(productsList[0])
    val selectedProduct = _selectedProduct.asStateFlow()

    /** Toggles the dropdown state. */
    fun toggleDropDown() {
        _isDropDownExpanded.value = !_isDropDownExpanded.value
    }

    /** Sets the selected product type. */
    fun setSelectedProduct(product: String) {
        _selectedProduct.value = product
    }

    /** State for product name */
    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    /** State for product selling price */
    private val _sellingPrice = MutableStateFlow("0.0")
    val sellingPrice = _sellingPrice.asStateFlow()

    /** State for product tax rate */
    private val _taxRate = MutableStateFlow("0.0")
    val taxRate = _taxRate.asStateFlow()

    /** State for product image */
    private val _image = MutableStateFlow<File?>(null)
    val image = _image.asStateFlow()

    /** State for representing UI loading state */
    private val _uiState = MutableStateFlow(UiState.IDLE)
    val uiState = _uiState.asStateFlow()

    /**
     * Sets the selected image file.
     * @param file The image file selected by the user.
     */
    fun setImage(file: File?) {
        _image.value = file
    }

    /**
     * Updates the product name.
     * @param name The new product name.
     */
    fun setName(name: String) {
        _name.value = name
    }

    /**
     * Updates the selling price.
     * @param sellingPrice The new selling price as a string.
     */
    fun setSellingPrice(sellingPrice: String) {
        _sellingPrice.value = sellingPrice
    }

    /**
     * Updates the tax rate.
     * @param taxRate The new tax rate as a string.
     */
    fun setTaxRate(taxRate: String) {
        _taxRate.value = taxRate
    }


    /**
     * Handles adding a new product. If offline, stores it locally and schedules a sync.
     * If online, sends the data to the server.
     *
     * @param goBack A lambda function to navigate back after product is added.
     */
    fun handleAddProduct(
        goBack: () -> Unit
    ) {
        val name = _name.value
        val productType = _selectedProduct.value
        val price = _sellingPrice.value.toDouble().toString()
        val tax = _taxRate.value.toDouble().toString()
        val file = _image.value

        // Validate inputs
        if (name.trim()
                .isEmpty() || price.toDouble() == "0.0".toDouble() || tax.toDouble() == "0.0".toDouble()
        ) {
            showToast(appContext, "Please fill all the fields")
            return
        }
        viewModelScope.launch {
            if (!isNetworkAvailable(appContext)) {
                // Store locally if no network
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
                // Send data to the server if network is available
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

    /**
     * Schedules a background sync task to upload locally stored products once the network is available.
     *
     * @param context The application context.
     */
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