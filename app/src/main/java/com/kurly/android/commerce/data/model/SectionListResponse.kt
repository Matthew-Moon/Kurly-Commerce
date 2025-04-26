package com.kurly.android.commerce.data.model

import com.google.gson.annotations.SerializedName

data class SectionListResponse(
    val data: List<Section>,
    val paging: Paging
)

data class Paging(
    @SerializedName("next_page")
    val nextPage: Int
)

data class Section(
    val id: Int,
    val title: String,
    val url: String,
    val type: String
)