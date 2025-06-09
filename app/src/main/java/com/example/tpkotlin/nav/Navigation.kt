package com.example.tpkotlin.nav

import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tpkotlin.ui.auth.AuthScreen
import com.example.tpkotlin.ui.product.ProductViewModel
import com.example.tpkotlin.ui.product.screens.HomeScreen
import com.example.tpkotlin.ui.product.screens.ProductDetailsScreen

object Routes {
    const val Home = "home"
    const val ProductDetails = "productDetails"
    const val Auth = "auth"
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppNavigation(viewModel: ProductViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Home) {

        composable(Routes.Home) {
            HomeScreen(
                viewModel,
                onNavigateToDetails = { productId ->
                    navController.navigate("${Routes.ProductDetails}/$productId")
                },
                onFavoriteClick = { productId ->
                    viewModel.toggleFavorite(productId)
                },
                onProfileClick = {
                    navController.navigate(Routes.Auth)
                }
            )
        }

        composable(
            "${Routes.ProductDetails}/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productIdStr = backStackEntry.arguments?.getString("productId") ?: ""
            val productId = productIdStr.toIntOrNull()
            val product = viewModel.state.value.products.find { it.productId == productId }
            if (product != null) {
                ProductDetailsScreen(product = product)
            } else {
                Text("Produit non trouvé.")
            }
        }

        composable(Routes.Auth) {
            AuthScreen()
        }
    }
}
