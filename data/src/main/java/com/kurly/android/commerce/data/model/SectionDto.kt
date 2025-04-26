package com.kurly.android.commerce.data.model

import com.google.gson.annotations.SerializedName

data class SectionListResponse(
    val data: List<SectionDto>,
    val paging: Paging
)

data class Paging(
    @SerializedName("next_page")
    val nextPage: Int
)

data class SectionDto(
    val id: Int,
    val title: String,
    val url: String,
    val type: String
)