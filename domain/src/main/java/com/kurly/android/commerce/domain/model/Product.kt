package com.kurly.android.commerce.domain.model

data class Product(
    val id: Long,
    val name: String,
    val image: String,
    val originalPrice: Int,
    val discountedPrice: Int? = null,
    val isSoldOut: Boolean
)