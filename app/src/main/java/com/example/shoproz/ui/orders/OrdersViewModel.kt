package com.example.shoproz.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoproz.data.dto.OrderDto
import com.example.shoproz.data.repo.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OrdersUiState(
    val loading: Boolean = false,
    val orders: List<OrderDto> = emptyList(),
    val error: String? = null
)

class OrdersViewModel(private val repo: OrderRepository) : ViewModel() {

    private val _state = MutableStateFlow(OrdersUiState())
    val state: StateFlow<OrdersUiState> = _state.asStateFlow()

    fun load() {
        _state.value = _state.value.copy(loading = true, error = null)
        viewModelScope.launch {
            try {
                val list = repo.list()
                _state.value = _state.value.copy(loading = false, orders = list)
            } catch (t: Throwable) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = t.message ?: "не удалось загрузить заказы"
                )
            }
        }
    }
}
