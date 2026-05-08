package com.example.shoproz.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoproz.data.dto.CategoryDto
import com.example.shoproz.data.dto.ProductDto
import com.example.shoproz.data.repo.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CatalogUiState(
    val loading: Boolean = false,
    val products: List<ProductDto> = emptyList(),
    val categories: List<CategoryDto> = emptyList(),
    val selectedCategory: Int? = null,
    val search: String = "",
    val error: String? = null
)

class CatalogViewModel(private val repo: ProductRepository) : ViewModel() {

    private val _state = MutableStateFlow(CatalogUiState())
    val state: StateFlow<CatalogUiState> = _state.asStateFlow()

    init {
        loadCategories()
        load()
    }

    fun setSearch(text: String) {
        _state.value = _state.value.copy(search = text)
    }

    fun applySearch() {
        load()
    }

    fun selectCategory(id: Int?) {
        _state.value = _state.value.copy(selectedCategory = id)
        load()
    }

    fun load() {
        _state.value = _state.value.copy(loading = true, error = null)
        viewModelScope.launch {
            try {
                val resp = repo.list(
                    categoryId = _state.value.selectedCategory,
                    search = _state.value.search.ifBlank { null },
                    page = 1,
                    limit = 50
                )
                _state.value = _state.value.copy(loading = false, products = resp.items)
            } catch (t: Throwable) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = t.message ?: "не удалось загрузить"
                )
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val cats = repo.categories()
                _state.value = _state.value.copy(categories = cats)
            } catch (_: Throwable) {
                // не критично, пропустим молча
            }
        }
    }
}
