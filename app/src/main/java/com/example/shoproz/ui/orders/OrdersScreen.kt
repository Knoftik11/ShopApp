package com.example.shoproz.ui.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shoproz.data.dto.OrderDto
import org.koin.androidx.compose.koinViewModel

@Composable
fun OrdersScreen(
    vm: OrdersViewModel = koinViewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) { vm.load() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Text("Мои заказы", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))

        when {
            state.loading && state.orders.isEmpty() -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            state.error != null && state.orders.isEmpty() -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(state.error ?: "", color = MaterialTheme.colorScheme.error)
            }
            state.orders.isEmpty() -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Заказов пока нет")
            }
            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.orders, key = { it.id }) { order ->
                    OrderRow(order)
                }
            }
        }
    }
}

@Composable
private fun OrderRow(order: OrderDto) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Заказ #${order.id}", style = MaterialTheme.typography.titleMedium)
                StatusBadge(order.status)
            }
            Spacer(Modifier.height(4.dp))
            Text(order.createdAt.replace('T', ' '), style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(4.dp))
            Text("Сумма: ${order.total} руб.", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(6.dp))
            order.items.forEach { item ->
                Text("• ${item.productName} ×${item.quantity}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val color = when (status) {
        "PENDING" -> Color(0xFFB8860B)
        "CONFIRMED" -> Color(0xFF1E88E5)
        "SHIPPED" -> Color(0xFF6A1B9A)
        "DELIVERED" -> Color(0xFF2E7D32)
        "CANCELLED" -> Color(0xFFC62828)
        else -> Color.DarkGray
    }
    Text(status, color = color, style = MaterialTheme.typography.labelLarge)
}
