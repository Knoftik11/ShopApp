package com.example.shoproz.data.repo

import com.example.shoproz.data.dto.UserDto
import com.example.shoproz.data.local.TokenStorage
import com.example.shoproz.data.remote.AuthApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class AuthRepository(
    private val api: AuthApi,
    private val tokens: TokenStorage
) {

    val isLoggedIn: Flow<Boolean> = tokens.accessTokenFlow.map { it != null }

    val currentUser: Flow<UserDto?> = combine(
        tokens.userNameFlow,
        tokens.userEmailFlow,
        tokens.userRoleFlow
    ) { name, email, role ->
        if (name != null && email != null && role != null) {
            UserDto(id = 0, name = name, email = email, role = role)
        } else null
    }

    suspend fun login(email: String, password: String) {
        val resp = api.login(email, password)
        tokens.saveTokens(resp.accessToken, resp.refreshToken)
        tokens.saveUser(resp.user.name, resp.user.email, resp.user.role)
    }

    suspend fun register(name: String, email: String, password: String) {
        val resp = api.register(name, email, password)
        tokens.saveTokens(resp.accessToken, resp.refreshToken)
        tokens.saveUser(resp.user.name, resp.user.email, resp.user.role)
    }

    suspend fun logout() {
        tokens.clear()
    }
}
