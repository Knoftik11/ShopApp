package com.example.shoproz.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoproz.data.repo.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val done: Boolean = false
)

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = AuthUiState(error = "заполни email и пароль")
            return
        }
        _state.value = AuthUiState(loading = true)
        viewModelScope.launch {
            try {
                repo.login(email.trim(), password)
                _state.value = AuthUiState(done = true)
            } catch (t: Throwable) {
                _state.value = AuthUiState(error = humanError(t))
            }
        }
    }

    fun register(name: String, email: String, password: String, confirm: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _state.value = AuthUiState(error = "заполни все поля")
            return
        }
        if (password != confirm) {
            _state.value = AuthUiState(error = "пароли не совпадают")
            return
        }
        if (password.length < 6) {
            _state.value = AuthUiState(error = "пароль слишком короткий")
            return
        }
        _state.value = AuthUiState(loading = true)
        viewModelScope.launch {
            try {
                repo.register(name.trim(), email.trim(), password)
                _state.value = AuthUiState(done = true)
            } catch (t: Throwable) {
                _state.value = AuthUiState(error = humanError(t))
            }
        }
    }

    fun reset() {
        _state.value = AuthUiState()
    }
}

private fun humanError(t: Throwable): String {
    val msg = t.message ?: "что-то пошло не так"
    return msg.take(200)
}
