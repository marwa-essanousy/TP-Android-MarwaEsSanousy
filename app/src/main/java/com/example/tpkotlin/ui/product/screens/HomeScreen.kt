package com.example.tpkotlin.ui.product.screens


import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tpkotlin.ui.product.ProductIntent
import com.example.tpkotlin.ui.product.ProductViewModel
import com.example.tpkotlin.ui.product.component.ProductsList

@Composable
fun HomeScreen(viewModel: ProductViewModel = viewModel(), onNavigateToDetails: (Int) -> Unit) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.handleIntent(ProductIntent.LoadProducts)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(CenterHorizontally))
            }

            state.error != null -> {
                Text(text = "Error: ${state.error}", color = Color.Red)
            }

            else -> {
                ProductsList(products = state.products, onNavigateToDetails)
            }
        }
    }
}
