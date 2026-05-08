package com.example.shoproz.data.remote

import com.example.shoproz.data.dto.CreateOrderRequest
import com.example.shoproz.data.dto.OrderDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class OrderApi(private val client: ApiClient) {

    suspend fun create(address: String?): OrderDto {
        return client.http.post("/orders") {
            setBody(CreateOrderRequest(address))
        }.body()
    }

    suspend fun list(): List<OrderDto> = client.http.get("/orders").body()

    suspend fun byId(id: Int): OrderDto = client.http.get("/orders/$id").body()
}
