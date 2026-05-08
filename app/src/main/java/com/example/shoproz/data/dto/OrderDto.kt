package com.example.shoproz.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderItemDto(
    val productId: Int,
    val productName: String,
    val quantity: Int,
    val priceAtTime: String
)

@Serializable
data class OrderDto(
    val id: Int,
    val status: String,
    val total: String,
    val address: String? = null,
    val createdAt: String,
    val items: List<OrderItemDto>
)

@Serializable
data class CreateOrderRequest(
    val address: String? = null
)
