package com.kurly.android.commerce.data.repository

import com.kurly.android.commerce.data.api.KurlyApi
import com.kurly.android.commerce.data.model.SectionListResponse
import com.kurly.android.commerce.data.model.SectionProductsResponse
import javax.inject.Inject

class KurlyRepository @Inject constructor(
    private val api: KurlyApi
) {
    suspend fun getSections(page: Int): SectionListResponse {
        return api.getSections(page)
    }

    suspend fun getSectionProducts(sectionId: Int): SectionProductsResponse {
        return api.getSectionProducts(sectionId)
    }
}