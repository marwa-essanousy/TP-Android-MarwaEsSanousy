package com.example.tpkotlin.nav

import AuthScreen
import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tpkotlin.data.Repository.SharedPreferencesManager
import com.example.tpkotlin.ui.cart.CartViewModel
import com.example.tpkotlin.ui.product.ProductViewModel
import com.example.tpkotlin.ui.product.screens.CartScreen
import com.example.tpkotlin.ui.product.screens.CheckoutScreen
import com.example.tpkotlin.ui.product.screens.FavoritesScreen
import com.example.tpkotlin.ui.product.screens.HomeScreen
import com.example.tpkotlin.ui.product.screens.OrdersScreen
import com.example.tpkotlin.ui.product.screens.ProductDetailsScreen
import com.example.tpkotlin.ui.product.screens.ProfileScreen
import com.example.tpkotlin.ui.product.screens.RegisterScreen
import com.example.tpkotlin.ui.product.screens.UserInfoScreen

object Routes {
    const val Home = "home"
    const val ProductDetails = "productDetails"
    const val Auth = "auth"
    const val Register = "register"
    const val Cart = "cart"
    const val Favorites = "favorites"
    const val Profile = "profile"
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppNavigation(viewModel: ProductViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val cartViewModel = remember { CartViewModel(context.applicationContext as android.app.Application) }
    val sharedPreferencesManager = remember { SharedPreferencesManager(context.applicationContext) }


    // Get cart item count
    val cartState = cartViewModel.state.value
    val cartItemCount = cartState.cartItems.size

    NavHost(navController = navController, startDestination = Routes.Home) {

        composable(Routes.Home) {
            HomeScreen(
                navController = navController,
                viewModel = viewModel,
                cartItemCount = cartItemCount,
                onNavigateToDetails = { productId ->
                    navController.navigate("${Routes.ProductDetails}/$productId")
                },
                onFavoriteClick = { productId ->
                    viewModel.toggleFavorite(productId)
                },
                onProfileClick = {
                    navController.navigate(Routes.Profile)
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
                ProductDetailsScreen(
                    product = product,
                    cartViewModel = cartViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else {
                Text("Produit non trouvé.")
            }
        }

        composable(Routes.Favorites) {
            FavoritesScreen(
                navController = navController,
                viewModel = viewModel,
                cartItemCount = cartItemCount,
                onNavigateToDetails = { productId ->
                    navController.navigate("${Routes.ProductDetails}/$productId")
                },
                onFavoriteClick = { productId ->
                    viewModel.toggleFavorite(productId)
                }
            )
        }

        composable(Routes.Auth) {
            val authViewModel = remember {
                com.example.tpkotlin.ui.product.AuthViewModel(context.applicationContext as android.app.Application).apply {
                    // Connect AuthViewModel to ProductViewModel
                    onUsernameUpdated = { username ->
                        viewModel.updateUsername(username)
                    }
                }
            }

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
                navController = navController,
                cartItems = cartState.cartItems,
                cartViewModel = cartViewModel
            )
        }

        composable(Routes.Profile) {
            val authViewModel = remember {
                com.example.tpkotlin.ui.product.AuthViewModel(context.applicationContext as android.app.Application).apply {
                    // Connect AuthViewModel to ProductViewModel
                    onUsernameUpdated = { username ->
                        viewModel.updateUsername(username)
                    }
                }
            }

            ProfileScreen(
                navController = navController,
                viewModel = authViewModel,
                cartItemCount = cartItemCount
            )
        }




        composable("user_info") {
            UserInfoScreen(navController) { fullName, phone, address ->
                navController.currentBackStackEntry?.savedStateHandle?.apply {
                    set("fullName", fullName)
                    set("phone", phone)
                    set("address", address)
                }
                navController.navigate("checkout")
            }
        }

        composable("checkout") {
            val fullName = navController.previousBackStackEntry?.savedStateHandle?.get<String>("fullName") ?: ""
            val phone = navController.previousBackStackEntry?.savedStateHandle?.get<String>("phone") ?: ""
            val address = navController.previousBackStackEntry?.savedStateHandle?.get<String>("address") ?: ""

            CheckoutScreen(
                navController = navController,
                cartItems = cartState.cartItems, // replace with your real cart
                cartViewModel = cartViewModel,
                sharedPreferencesManager =sharedPreferencesManager,
                userFullName = fullName,
                userPhone = phone,
                userAddress = address
            )
        }

        composable("orders") {
            OrdersScreen(navController)
        }
    }
}
