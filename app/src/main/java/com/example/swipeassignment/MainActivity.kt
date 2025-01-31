package com.example.swipeassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.swipeassignment.ui.screens.add_products_screen.AddProductScreen
import com.example.swipeassignment.ui.screens.products_screen.ProductsScreen
import com.example.swipeassignment.ui.theme.SwipeassignmentTheme
import kotlinx.serialization.Serializable

val LocalNavController = compositionLocalOf<NavController> { error("NavController not found!") }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SwipeassignmentTheme {
                val navController = rememberNavController()
                CompositionLocalProvider(LocalNavController provides navController) {
                    NavHost(navController = navController, startDestination = Products){
                        composable<Products>{
                            ProductsScreen()
                        }
                        composable<AddProduct> {
                            AddProductScreen()
                        }
                    }
                }
            }
        }
    }
}

@Serializable
object Products

@Serializable
object AddProduct