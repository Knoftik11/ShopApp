package com.example.shoproz.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class TokenStorage(private val context: Context) {

    private val accessKey = stringPreferencesKey("access")
    private val refreshKey = stringPreferencesKey("refresh")
    private val userNameKey = stringPreferencesKey("user_name")
    private val userEmailKey = stringPreferencesKey("user_email")
    private val userRoleKey = stringPreferencesKey("user_role")

    val accessTokenFlow: Flow<String?> = context.dataStore.data.map { it[accessKey] }
    val refreshTokenFlow: Flow<String?> = context.dataStore.data.map { it[refreshKey] }
    val userNameFlow: Flow<String?> = context.dataStore.data.map { it[userNameKey] }
    val userEmailFlow: Flow<String?> = context.dataStore.data.map { it[userEmailKey] }
    val userRoleFlow: Flow<String?> = context.dataStore.data.map { it[userRoleKey] }

    suspend fun getAccessToken(): String? = accessTokenFlow.first()
    suspend fun getRefreshToken(): String? = refreshTokenFlow.first()

    suspend fun saveTokens(access: String, refresh: String) {
        context.dataStore.edit { p ->
            p[accessKey] = access
            p[refreshKey] = refresh
        }
    }

    suspend fun saveUser(name: String, email: String, role: String) {
        context.dataStore.edit { p ->
            p[userNameKey] = name
            p[userEmailKey] = email
            p[userRoleKey] = role
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
