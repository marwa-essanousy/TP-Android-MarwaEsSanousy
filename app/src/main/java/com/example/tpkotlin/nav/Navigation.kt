package com.example.tpkotlin.nav

import AuthScreen
import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tpkotlin.ui.cart.CartViewModel
import com.example.tpkotlin.ui.product.ProductViewModel
import com.example.tpkotlin.ui.product.screens.CartScreen
import com.example.tpkotlin.ui.product.screens.FavoritesScreen
import com.example.tpkotlin.ui.product.screens.HomeScreen
import com.example.tpkotlin.ui.product.screens.ProductDetailsScreen
import com.example.tpkotlin.ui.product.screens.RegisterScreen



object Routes {
    const val Home = "home"
    const val ProductDetails = "productDetails"
    const val Auth = "auth"
    const val Register = "register"
    const val Cart = "cart"
    const val Favorites = "favorites"


}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppNavigation(viewModel: ProductViewModel, cartViewModel: CartViewModel = viewModel()) {
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
                },
                onCartClick = {
                    navController.navigate(Routes.Cart)
                },
                onFavoritesClick = {
                    navController.navigate(Routes.Favorites)
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
                ProductDetailsScreen(product = product, cartViewModel = cartViewModel)
            } else {
                Text("Produit non trouvé.")
            }
        }
        composable(Routes.Favorites) {
            FavoritesScreen(
                viewModel = viewModel,
                onNavigateToDetails = { productId ->
                    navController.navigate("${Routes.ProductDetails}/$productId")
                },
                onFavoriteClick = { productId ->
                    viewModel.toggleFavorite(productId)
                }
            )
        }

        composable(Routes.Auth) {
            AuthScreen(
                navController = navController,
                onRegisterClick = {
                    navController.navigate(Routes.Register)
                }
            )
        }

        composable(Routes.Register) {
            RegisterScreen(onLoginClick = {
                navController.popBackStack()
            })
        }

        composable(Routes.Cart) {
            val cartState = cartViewModel.state.value
            CartScreen(
                cartItems = cartState.cartItems,
                cartViewModel = cartViewModel
            )
        }


        composable(Routes.Favorites) {
            FavoritesScreen(
                viewModel = viewModel,
                onFavoriteClick = { productId -> viewModel.toggleFavorite(productId) },
                onNavigateToDetails = { productId ->
                    navController.navigate("${Routes.ProductDetails}/$productId")
                }
            )
        }


    }
}
