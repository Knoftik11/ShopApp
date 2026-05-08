package com.example.shoproz.data.repo

import com.example.shoproz.data.dto.CategoryDto
import com.example.shoproz.data.dto.PageResponse
import com.example.shoproz.data.dto.ProductDto
import com.example.shoproz.data.remote.ProductApi

class ProductRepository(private val api: ProductApi) {

    suspend fun list(
        categoryId: Int? = null,
        search: String? = null,
        page: Int = 1,
        limit: Int = 20
    ): PageResponse<ProductDto> = api.list(categoryId, search, page, limit)

    suspend fun byId(id: Int): ProductDto = api.byId(id)

    suspend fun categories(): List<CategoryDto> = api.categories()
}
