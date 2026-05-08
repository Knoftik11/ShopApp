package com.example.shoproz.data.remote

import com.example.shoproz.data.dto.CategoryDto
import com.example.shoproz.data.dto.PageResponse
import com.example.shoproz.data.dto.ProductDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ProductApi(private val client: ApiClient) {

    suspend fun list(
        categoryId: Int? = null,
        search: String? = null,
        page: Int = 1,
        limit: Int = 20
    ): PageResponse<ProductDto> {
        return client.http.get("/products") {
            if (categoryId != null) parameter("category", categoryId)
            if (!search.isNullOrBlank()) parameter("search", search)
            parameter("page", page)
            parameter("limit", limit)
        }.body()
    }

    suspend fun byId(id: Int): ProductDto {
        return client.http.get("/products/$id").body()
    }

    suspend fun categories(): List<CategoryDto> {
        return client.http.get("/categories").body()
    }
}
