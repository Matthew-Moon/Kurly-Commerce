package com.kurly.android.commerce.data.model

import com.google.gson.annotations.SerializedName

data class SectionProductsResponse(
    val data: List<ProductDto>
)

data class ProductDto(
    val id: Long,
    val name: String,
    val image: String,
    val originalPrice: Int,
    @SerializedName("discountedPrice")
    val discountedPrice: Int? = null,
    @SerializedName("is_sold_out")
    val isSoldOut: Boolean
)