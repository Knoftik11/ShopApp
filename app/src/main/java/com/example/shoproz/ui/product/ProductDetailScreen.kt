package com.example.shoproz.ui.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductDetailScreen(
    productId: Int,
    onCartClick: () -> Unit,
    vm: ProductDetailViewModel = koinViewModel()
) {
    val state by vm.state.collectAsState()
    val scroll = rememberScrollState()

    LaunchedEffect(productId) {
        vm.load(productId)
    }

    when {
        state.loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        state.error != null && state.product == null -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                state.error ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
        state.product != null -> {
            val p = state.product!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scroll)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (!p.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = p.imageUrl,
                        contentDescription = p.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    )
                }
                Text(p.name, style = MaterialTheme.typography.headlineSmall)
                Text("${p.price} руб.", style = MaterialTheme.typography.titleLarge)
                Text(
                    if (p.stock > 0) "В наличии: ${p.stock}" else "Нет в наличии",
                    color = if (p.stock > 0) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(4.dp))
                Text(p.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))

                if (state.addedToCart) {
                    Text("Добавлено в корзину", color = MaterialTheme.colorScheme.primary)
                }
                if (state.error != null && state.product != null) {
                    Text(state.error ?: "", color = MaterialTheme.colorScheme.error)
                }

                Button(
                    onClick = { vm.addToCart() },
                    enabled = p.stock > 0 && !state.addingToCart,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (state.addingToCart) "..." else "В корзину")
                }
                Button(
                    onClick = onCartClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Перейти в корзину")
                }
            }
        }
    }
}
