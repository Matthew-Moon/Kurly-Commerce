// data/src/main/java/com/kurly/android/commerce/data/mapper/DataMapper.kt
package com.kurly.android.commerce.data.mapper

import com.kurly.android.commerce.data.model.SectionDto
import com.kurly.android.commerce.data.model.ProductDto
import com.kurly.android.commerce.domain.model.Section
import com.kurly.android.commerce.domain.model.Product

// Section 매핑
fun SectionDto.toSection(): Section {
    return Section(
        id = id,
        title = title,
        url = url,
        type = type
    )
}

// Product 매핑
fun ProductDto.toProduct(): Product {
    return Product(
        id = id,
        name = name,
        image = image,
        originalPrice = originalPrice,
        discountedPrice = discountedPrice,
        isSoldOut = isSoldOut,
    )
}