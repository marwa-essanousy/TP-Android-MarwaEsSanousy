package com.example.tpkotlin.data.Repository



import com.example.tpkotlin.R
import com.example.tpkotlin.data.Entities.Product
import kotlinx.coroutines.delay

class ProductRepository {
    suspend fun getProducts(): List<Product> {
        // Simulate fetching data from a remote server or database
        delay(2000)
        return listOf(
            Product(
                _id = "1",
                productId = 1,
                productTitle = "Hydrating Face Cream",
                productImage = R.drawable.creme_hydratante ,
                productPrice = 250,
                quantity = 50,
                category = "Facial care",
                description =  "Deeply nourishing cream for dry skin with organic aloe vera extract. Provides 24-hour hydration."
            ),
            Product(
                _id = "2",
                productId = 2,
                productTitle =  "Advanced Sun Protection SPF 50+",
                productImage = R.drawable.ecran_solaire,
                productPrice = 180,
                quantity = 35,
                category = "Facial care",
                description = "Lightweight broad-spectrum UVA/UVB protection with PA++++ rating. " +
                        "Water-resistant for up to 80 minutes, non-greasy formula enriched with " +
                        "vitamin E. Suitable for all skin types, including sensitive skin. " +
                        "Provides antioxidant protection against environmental damage."
            ),
            Product(
                _id = "3",
                productId = 3,
                productTitle = "Matte Liquid Lipstick",
                productImage = R.drawable.lipstick,
                productPrice = 150,
                quantity = 60,
                category = "Makeup",
                description = "Long-lasting matte lip color with vitamin E. Transfer-proof formula available in 12 shades."
            ),
            Product(
                _id = "4",
                productId = 4,
                productTitle = "Nude Eyeshadow Palette",
                productImage = R.drawable.eyeshadow,
                productPrice = 70,
                quantity = 25,
                category = "Makeup",
                description = "10 versatile neutral shades with matte and shimmer finishes. Cruelty-free and blendable."
            ),
            Product(
                _id = "5",
                productId = 5,
                productTitle = "Relaxing Shower Gel",
                productImage = R.drawable.gel,
                productPrice = 30,
                quantity = 80,
                category = "Facial care",
                description =  "Soothing lavender-infused shower gel that gently cleanses without stripping natural oils."
            )
        )
    }
}