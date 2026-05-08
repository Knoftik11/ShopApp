package com.example.shoproz.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoproz.data.dto.ProductDto
import com.example.shoproz.data.repo.CartRepository
import com.example.shoproz.data.repo.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProductDetailUiState(
    val loading: Boolean = false,
    val product: ProductDto? = null,
    val error: String? = null,
    val addingToCart: Boolean = false,
    val addedToCart: Boolean = false
)

class ProductDetailViewModel(
    private val products: ProductRepository,
    private val cart: CartRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailUiState())
    val state: StateFlow<ProductDetailUiState> = _state.asStateFlow()

    fun load(id: Int) {
        _state.value = ProductDetailUiState(loading = true)
        viewModelScope.launch {
            try {
                val p = products.byId(id)
                _state.value = ProductDetailUiState(product = p)
            } catch (t: Throwable) {
                _state.value = ProductDetailUiState(error = t.message ?: "не удалось загрузить")
            }
        }
    }

    fun addToCart() {
        val p = _state.value.product ?: return
        _state.value = _state.value.copy(addingToCart = true, addedToCart = false)
        viewModelScope.launch {
            try {
                cart.add(p.id, 1)
                _state.value = _state.value.copy(addingToCart = false, addedToCart = true)
            } catch (t: Throwable) {
                _state.value = _state.value.copy(
                    addingToCart = false,
                    error = t.message ?: "ошибка добавления в корзину"
                )
            }
        }
    }
}
