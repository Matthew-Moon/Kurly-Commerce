package com.kurly.android.commerce.domain.repository

import com.kurly.android.commerce.domain.model.Product
import com.kurly.android.commerce.domain.model.Section
import kotlinx.coroutines.flow.Flow

interface KurlyRepository {
//    fun getSections(pageSize: Int): Flow<List<Section>>
    suspend fun getSectionProducts(sectionId: Int): List<Product>
}