package com.example.shoproz.data.repo

import com.example.shoproz.data.dto.CartDto
import com.example.shoproz.data.remote.CartApi

class CartRepository(private val api: CartApi) {

    suspend fun get(): CartDto = api.get()

    suspend fun add(productId: Int, quantity: Int = 1): CartDto = api.add(productId, quantity)

    suspend fun removeItem(itemId: Int) {
        api.removeItem(itemId)
    }

    suspend fun clear() {
        api.clear()
    }
}
