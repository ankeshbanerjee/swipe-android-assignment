package com.example.swipeassignment.ui.screens.products_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swipeassignment.AddProduct
import com.example.swipeassignment.LocalNavController
import com.example.swipeassignment.data.network.model.product.Product
import com.example.swipeassignment.ui.common.shared_components.LoadingComponent
import com.example.swipeassignment.ui.screens.products_screen.components.ProductItem
import com.example.swipeassignment.utils.UiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel = koinViewModel()
) {
    val products = viewModel.products.collectAsState()
    val uiState = viewModel.uiState.collectAsState()
    val navController = LocalNavController.current
    val navigateToAddProduct = {
        navController.navigate(AddProduct)
    }

    ProductsScreenContent(
        products = products.value,
        uiState = uiState.value,
        navigateToAddProduct = navigateToAddProduct
    )
}

@Composable
private fun ProductsScreenContent(
    products: List<Product>,
    uiState: UiState,
    navigateToAddProduct: () -> Unit,
) {

    if (uiState == UiState.LOADING){
        LoadingComponent()
        return
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigateToAddProduct()
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                "All Products",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            )
            LazyColumn {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }
                items(products.size) { index ->
                    ProductItem(
                        product = products[index]
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProductsScreenPreview() {
    ProductsScreenContent(
        uiState = UiState.LOADING,
        navigateToAddProduct = {},
        products = listOf(
            Product(
                image = "",
                price = 567.0,
                product_name = "bjj",
                product_type = "General",
                tax = 577.0
            ),
            Product(
                image = "https://vx-erp-product-images.s3.ap-south-1.amazonaws.com/9_1738262418_0_temp_image.jpg",
                price = 1.0,
                product_name = "farmer",
                product_type = "General",
                tax = 2.0
            ),
            Product(
                image = "",
                price = 70.0,
                product_name = "tby",
                product_type = "Vehicles",
                tax = 7.0
            ),
            Product(
                image = "https://vx-erp-product-images.s3.ap-south-1.amazonaws.com/9_1738261144_0_temp_image.jpg",
                price = 400.0,
                product_name = "Notification",
                product_type = "Home Appliances",
                tax = 40.0
            ),
            Product(
                image = "",
                price = 1200.0,
                product_name = "Taxi ride",
                product_type = "Service",
                tax = 10.0
            )
        )
    )
}