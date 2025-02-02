package com.example.swipeassignment.ui.screens.add_products_screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.swipeassignment.LocalNavController
import com.example.swipeassignment.Products
import com.example.swipeassignment.ui.common.shared_components.CustomOutlinedTextInput
import com.example.swipeassignment.ui.common.shared_components.FullScreenDialog
import com.example.swipeassignment.utils.UiState
import com.example.swipeassignment.utils.getFileFromUri
import org.koin.androidx.compose.koinViewModel
import java.io.File

/**
 * Composable function for adding a product.
 * @param vm ViewModel instance of [AddProductViewModel].
 */
@Composable
fun AddProductScreen(vm: AddProductViewModel = koinViewModel()) {
    val name by vm.name.collectAsState()
    val sellingPrice by vm.sellingPrice.collectAsState()
    val taxRate by vm.taxRate.collectAsState()
    val isDropDownExpanded by vm.isDropDownExpanded.collectAsState()
    val selectedProduct by vm.selectedProduct.collectAsState()
    val productsList = vm.productsList
    val setSelectedProduct = vm::setSelectedProduct
    val toggleDropDown = vm::toggleDropDown
    val setImage = vm::setImage
    val navController = LocalNavController.current
    val navigateBack = {
        navController.popBackStack()
    }
    val image by vm.image.collectAsState()
    val uiState by vm.uiState.collectAsState()
    val handleAddProduct = {
        vm.handleAddProduct {
            navController.navigate(Products) {
                popUpTo(0)
            }
        }
    }

    AddProductScreenContent(
        name = name,
        sellingPrice = sellingPrice,
        taxRate = taxRate,
        setName = vm::setName,
        setSellingPrice = {
            vm.setSellingPrice(it)
        },
        setTaxRate = {
            vm.setTaxRate(it)
        },
        navigateBack = {
            navigateBack()
        },
        isDropDownExpanded = isDropDownExpanded,
        selectedProduct = selectedProduct,
        productsList = productsList,
        setSelectedProduct = setSelectedProduct,
        toggleDropDown = toggleDropDown,
        image = image,
        setImage = setImage,
        uiState = uiState,
        handleAddProduct = handleAddProduct
    )
}

/**
 * UI content for the Add Product screen.
 * @param name Product name.
 * @param sellingPrice Selling price of the product.
 * @param taxRate Tax rate of the product.
 * @param setName Callback to update product name.
 * @param setSellingPrice Callback to update selling price.
 * @param setTaxRate Callback to update tax rate.
 * @param navigateBack Callback to navigate back.
 * @param isDropDownExpanded State for dropdown expansion.
 * @param selectedProduct Currently selected product.
 * @param productsList List of available products.
 * @param setSelectedProduct Callback to set selected product.
 * @param toggleDropDown Callback to toggle dropdown.
 * @param image Selected product image file.
 * @param setImage Callback to update the image file.
 * @param uiState UI state for loading indicator.
 * @param handleAddProduct Callback to handle adding a product.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreenContent(
    name: String,
    sellingPrice: String,
    taxRate: String,
    setName: (String) -> Unit,
    setSellingPrice: (String) -> Unit,
    setTaxRate: (String) -> Unit,
    navigateBack: () -> Unit,
    isDropDownExpanded: Boolean,
    selectedProduct: String,
    productsList: List<String>,
    setSelectedProduct: (String) -> Unit,
    toggleDropDown: () -> Unit,
    image: File?,
    setImage: (File?) -> Unit,
    uiState: UiState,
    handleAddProduct: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Add Product", fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable {
                            navigateBack()
                        })
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    ) { it ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(it)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            Box {
                var dropDownMenuSize by remember { mutableStateOf(Size.Zero) }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .border(1.dp, MaterialTheme.colorScheme.secondaryContainer)
                        .clickable {
                            toggleDropDown()
                        }
                        .onGloballyPositioned { coords ->
                            dropDownMenuSize = coords.size.toSize()
                        }
                        .padding(16.dp)
                        .fillMaxWidth()

                ) {
                    Text(text = selectedProduct)
                    Icon(
                        if (isDropDownExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = isDropDownExpanded,
                    onDismissRequest = { toggleDropDown() },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { dropDownMenuSize.width.toDp() })
                ) {
                    productsList.forEach { label ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                setSelectedProduct(label)
                                toggleDropDown()
                            })
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            CustomOutlinedTextInput(
                label = "Product Name",
                value = name,
                onValueChange = setName,
                placeholder = "Enter Product Name"
            )
            Spacer(modifier = Modifier.height(12.dp))
            CustomOutlinedTextInput(
                label = "Selling Price",
                value = sellingPrice,
                onValueChange = setSellingPrice,
                placeholder = "Enter Selling Price",
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(12.dp))
            CustomOutlinedTextInput(
                label = "Tax Rate",
                value = taxRate,
                onValueChange = setTaxRate,
                placeholder = "Enter Tax Rate",
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Image selection button
            val context = LocalContext.current
            val pickMedia =
                rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                    if (uri != null) {
                        val file =
                            getFileFromUri(contentResolver = context.contentResolver, uri = uri)
                        setImage(file)
                    }
                }
            Button(
                onClick = {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                modifier = Modifier.padding(16.dp),
                enabled = true,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    containerColor = Color.Transparent
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
            ) {
                Text(
                    text = "Choose Image",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            image?.let {
                Text(
                    text = "Image Selected",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            // Add product button
            Button(
                onClick = {
                    handleAddProduct()
                },
                modifier = Modifier.padding(16.dp),
                enabled = true,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
            ) {
                Text(
                    text = "Add Product",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        FullScreenDialog(uiState == UiState.LOADING)
    }
}

/**
 * Preview composable for Add Product screen.
 */
@Preview
@Composable
private fun AddProductScreenPreview() {
    AddProductScreenContent(
        name = "Product Name",
        sellingPrice = "0.0",
        taxRate = "0.0",
        setName = {},
        setSellingPrice = {},
        setTaxRate = {},
        navigateBack = {},
        isDropDownExpanded = false,
        selectedProduct = "Product",
        productsList = listOf("Product", "Service", "Tax"),
        setSelectedProduct = {},
        toggleDropDown = {},
        setImage = {},
        image = null,
        uiState = UiState.LOADING,
        handleAddProduct = {}
    )
}