package com.example.shoproz.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Int,
    val name: String,
    val description: String,
    val price: String,
    val stock: Int,
    val categoryId: Int? = null,
    val imageUrl: String? = null
)

@Serializable
data class CategoryDto(
    val id: Int,
    val name: String,
    val parentId: Int? = null
)

@Serializable
data class PageResponse<T>(
    val items: List<T>,
    val page: Int,
    val limit: Int,
    val total: Long
)
