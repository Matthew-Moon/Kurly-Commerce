package com.kurly.android.commerce.domain.usecase

import com.kurly.android.commerce.domain.model.Product
import com.kurly.android.commerce.domain.repository.KurlyRepository

class GetSectionProductsUseCase(private val repository: KurlyRepository) {

    suspend operator fun invoke(sectionId: Int): List<Product> {
        return repository.getSectionProducts(sectionId)
    }
}