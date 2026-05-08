package com.example.shoproz.data.remote

import com.example.shoproz.data.dto.AuthResponse
import com.example.shoproz.data.dto.RefreshRequest
import com.example.shoproz.data.local.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.call.body
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class ApiClient(private val tokens: TokenStorage) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    val http: HttpClient = HttpClient(Android) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(json)
        }
        install(Logging)
        install(DefaultRequest) {
            url(ApiConfig.BASE_URL)
            contentType(ContentType.Application.Json)
        }
        install(Auth) {
            bearer {
                loadTokens {
                    val a = tokens.getAccessToken()
                    val r = tokens.getRefreshToken()
                    if (a != null && r != null) BearerTokens(a, r) else null
                }
                refreshTokens {
                    val r = tokens.getRefreshToken() ?: return@refreshTokens null
                    val resp: HttpResponse = client.post("/auth/refresh") {
                        markAsRefreshTokenRequest()
                        setBody(RefreshRequest(r))
                    }
                    val body: AuthResponse = resp.body()
                    tokens.saveTokens(body.accessToken, body.refreshToken)
                    BearerTokens(body.accessToken, body.refreshToken)
                }
                sendWithoutRequest { req ->
                    val first = req.url.pathSegments.firstOrNull { it.isNotBlank() }
                    first != "auth"
                }
            }
        }
    }

    fun close() {
        http.close()
    }
}

@Suppress("unused")
private fun unusedSuppressRunBlocking() = runBlocking {}
