package com.example.shoproz.ui.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun CheckoutScreen(
    onPlaced: (Int) -> Unit,
    vm: CartViewModel = koinViewModel()
) {
    val state by vm.state.collectAsState()
    var address by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        vm.load()
    }

    LaunchedEffect(state.orderPlacedId) {
        val id = state.orderPlacedId
        if (id != null) {
            onPlaced(id)
            vm.resetOrderPlaced()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Оформление заказа", style = MaterialTheme.typography.headlineSmall)

        val cart = state.cart
        if (cart != null) {
            Text("Сумма: ${cart.total} руб.", style = MaterialTheme.typography.titleMedium)
            Text("Позиций: ${cart.items.sumOf { it.quantity }}")
        }

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Адрес доставки") },
            modifier = Modifier.fillMaxWidth()
        )

        if (state.error != null) {
            Text(state.error ?: "", color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = { vm.placeOrder(address) },
            enabled = !state.placingOrder && cart != null && cart.items.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (state.placingOrder) "Отправка..." else "Подтвердить")
        }
    }
}
