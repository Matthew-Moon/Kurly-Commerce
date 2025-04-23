package com.kurly.android.commerce.ui.home.model

data class SectionUiModel(
    val id: Int,
    val title: String,
    val type: String,
    val products: List<ProductUiModel> = emptyList()
)

data class ProductUiModel(
    val id: Long,
    val name: String,
    val image: String,
    val originalPrice: Int,
    val discountedPrice: Int? = null,
    val isSoldOut: Boolean
)

// 섹션 타입 상수
object SectionType {
    const val HORIZONTAL = "horizontal"  // 가로 스크롤
    const val VERTICAL = "vertical"      // 세로 스크롤
    const val GRID = "grid"             // 그리드 형태
} 