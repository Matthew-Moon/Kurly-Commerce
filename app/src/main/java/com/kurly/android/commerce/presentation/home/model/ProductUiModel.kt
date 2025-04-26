package com.kurly.android.commerce.presentation.home.model

import com.kurly.android.commerce.domain.model.Product
import kotlin.math.roundToInt

data class ProductUiModel(
    val id: Long,
    val name: String,
    val image: String,
    val originalPrice: Int,
    val discountedPrice: Int?,
    val isSoldOut: Boolean,
    val isFavorite: Boolean = false,
    val sectionId: Int = 0
) {
    // 할인율 계산 함수 추가
    fun getDiscountRate(): Int {
        return if (discountedPrice != null && discountedPrice < originalPrice) {
            val discountAmount = originalPrice - discountedPrice
            ((discountAmount.toFloat() / originalPrice.toFloat()) * 100).roundToInt()
        } else {
            0
        }
    }
    
    // 할인이 적용된 상품인지 확인
    fun hasDiscount(): Boolean = getDiscountRate() > 0
}

// 도메인 모델을 UI 모델로 변환하는 확장 함수
fun Product.toUiModel(isFavorite: Boolean = false, sectionId: Int = 0): ProductUiModel {
    return ProductUiModel(
        id = this.id,
        name = this.name,
        image = this.image,
        originalPrice = this.originalPrice,
        discountedPrice = this.discountedPrice,
        isSoldOut = this.isSoldOut,
        isFavorite = isFavorite,
        sectionId = sectionId
    )
} 