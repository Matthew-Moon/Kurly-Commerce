package com.kurly.android.commerce.data.model

data class SectionListResponse(
    val data: List<Section>,
    val paging: Paging
)

data class Paging(
    val next_page: Int
)

data class Section(
    val id: Int,
    val title: String,
    val url: String,
    val type: String
)