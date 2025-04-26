package com.kurly.android.commerce.presentation.home.model

import com.kurly.android.commerce.domain.model.Section

data class SectionUiModel(
    val id: Int,
    val title: String,
    val type: String,
    val products: List<ProductUiModel> = emptyList()
)

// 도메인 모델을 UI 모델로 변환하는 확장 함수
fun Section.toUiModel(): SectionUiModel {
    return SectionUiModel(
        id = this.id,
        title = this.title,
        type = this.type
    )
} 