package com.example.swipeassignment

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
            0
        )
        enableEdgeToEdge()
        setContent {
            SwipeassignmentTheme {
                val navController = rememberNavController()
                CompositionLocalProvider(LocalNavController provides navController) {
                    NavHost(navController = navController, startDestination = Products) {
                        composable<Products> {
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