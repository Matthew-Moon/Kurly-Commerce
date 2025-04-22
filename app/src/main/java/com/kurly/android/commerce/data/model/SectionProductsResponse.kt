package com.kurly.android.commerce.data.model

data class SectionProductsResponse(
    val data: List<Product>
)

data class Product(
    val id: Long,
    val name: String,
    val image: String,
    val originalPrice: Int,
    val discountedPrice: Int? = null,
    val isSoldOut: Boolean
)