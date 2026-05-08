package com.example.shoproz.data.remote

import com.example.shoproz.data.dto.AddToCartRequest
import com.example.shoproz.data.dto.CartDto
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class CartApi(private val client: ApiClient) {

    suspend fun get(): CartDto = client.http.get("/cart").body()

    suspend fun add(productId: Int, quantity: Int = 1): CartDto {
        return client.http.post("/cart") {
            setBody(AddToCartRequest(productId, quantity))
        }.body()
    }

    suspend fun removeItem(itemId: Int) {
        client.http.delete("/cart/$itemId")
    }

    suspend fun clear() {
        client.http.delete("/cart")
    }
}
