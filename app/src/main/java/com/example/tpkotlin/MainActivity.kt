package com.example.tpkotlin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tpkotlin.ui.theme.TpKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TpKotlinTheme {
              MyAppNavHost()
            }
        }
    }
}
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MyAppNavHost(viewModel: ProductsViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = routes.Products.route) {
        composable(routes.Products.route) {
            ProductsScreen(viewModel, navController)
        }

        composable(
            route = "${routes.ProductDetails.route}/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId")
            val product = viewModel.products.value.find { it.productId == productId }
            product?.let {
                ProductDetailsScreen(it)
            }
        }
    }
}