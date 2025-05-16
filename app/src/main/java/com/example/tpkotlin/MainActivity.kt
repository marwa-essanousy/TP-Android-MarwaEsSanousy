package com.example.tpkotlin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tpkotlin.nav.AppNavigation
import com.example.tpkotlin.ui.product.ProductViewModel
import com.example.tpkotlin.ui.product.screens.ProductDetailsScreen
import com.example.tpkotlin.ui.theme.TpKotlinTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ProductViewModel by viewModels<ProductViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TpKotlinTheme {
                AppNavigation(viewModel)
            }
        }
    }
}