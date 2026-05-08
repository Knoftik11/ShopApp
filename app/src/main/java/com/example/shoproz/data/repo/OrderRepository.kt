package com.example.shoproz.data.repo

import com.example.shoproz.data.dto.OrderDto
import com.example.shoproz.data.remote.OrderApi

class OrderRepository(private val api: OrderApi) {

    suspend fun create(address: String?): OrderDto = api.create(address)

    suspend fun list(): List<OrderDto> = api.list()

    suspend fun byId(id: Int): OrderDto = api.byId(id)
}
