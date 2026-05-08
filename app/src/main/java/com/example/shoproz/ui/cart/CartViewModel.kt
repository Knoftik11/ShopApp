package com.example.shoproz.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoproz.data.dto.CartDto
import com.example.shoproz.data.repo.CartRepository
import com.example.shoproz.data.repo.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CartUiState(
    val loading: Boolean = false,
    val cart: CartDto? = null,
    val error: String? = null,
    val placingOrder: Boolean = false,
    val orderPlacedId: Int? = null
)

class CartViewModel(
    private val cart: CartRepository,
    private val orders: OrderRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CartUiState())
    val state: StateFlow<CartUiState> = _state.asStateFlow()

    fun load() {
        _state.value = _state.value.copy(loading = true, error = null)
        viewModelScope.launch {
            try {
                val c = cart.get()
                _state.value = _state.value.copy(loading = false, cart = c)
            } catch (t: Throwable) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = t.message ?: "не удалось загрузить корзину"
                )
            }
        }
    }

    fun remove(itemId: Int) {
        viewModelScope.launch {
            try {
                cart.removeItem(itemId)
                load()
            } catch (t: Throwable) {
                _state.value = _state.value.copy(error = t.message ?: "ошибка удаления")
            }
        }
    }

    fun placeOrder(address: String?) {
        _state.value = _state.value.copy(placingOrder = true, orderPlacedId = null, error = null)
        viewModelScope.launch {
            try {
                val o = orders.create(address?.takeIf { it.isNotBlank() })
                _state.value = _state.value.copy(placingOrder = false, orderPlacedId = o.id, cart = null)
            } catch (t: Throwable) {
                _state.value = _state.value.copy(
                    placingOrder = false,
                    error = t.message ?: "ошибка оформления"
                )
            }
        }
    }

    fun resetOrderPlaced() {
        _state.value = _state.value.copy(orderPlacedId = null)
    }
}
