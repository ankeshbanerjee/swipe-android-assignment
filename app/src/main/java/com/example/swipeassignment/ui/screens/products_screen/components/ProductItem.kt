package com.example.swipeassignment.ui.screens.products_screen.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.swipeassignment.data.network.model.product.Product

@Composable
fun ProductItem(
    product: Product
) {
    ProductItemContent(product)
}

@Composable
private fun ProductItemContent(product: Product) {
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(bottom = 10.dp)
            .shadow(6.dp, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .height(110.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(product.image.ifEmpty { "https://karanzi.websites.co.in/obaju-turquoise/img/product-placeholder.png" })
                .crossfade(true)
                .build(),
            loading = {
                CircularProgressIndicator()
            },
            onError = {
                Log.e("IMAGE_ERROR", "Error loading image - ${it.result}")
            },
            contentDescription = "product_image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .height(100.dp)
                .width(100.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)

        )
        Spacer(modifier = Modifier.width(20.dp))
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(product.product_name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(product.product_type)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Price: ${product.price}")
                Text("Tax: ${product.tax}")
            }
        }
    }
}

@Preview
@Composable
private fun ProductItemPreview() {
    ProductItemContent(
        Product(
            image = "",
            price = 567.0,
            product_name = "bjj",
            product_type = "General",
            tax = 577.0
        )
    )
}