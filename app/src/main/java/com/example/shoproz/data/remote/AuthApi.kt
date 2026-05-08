package com.example.shoproz.data.remote

import com.example.shoproz.data.dto.AuthResponse
import com.example.shoproz.data.dto.LoginRequest
import com.example.shoproz.data.dto.RegisterRequest
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthApi(private val client: ApiClient) {

    suspend fun register(name: String, email: String, password: String): AuthResponse {
        return client.http.post("/auth/register") {
            setBody(RegisterRequest(name = name, email = email, password = password))
        }.body()
    }

    suspend fun login(email: String, password: String): AuthResponse {
        return client.http.post("/auth/login") {
            setBody(LoginRequest(email = email, password = password))
        }.body()
    }
}
