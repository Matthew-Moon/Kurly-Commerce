package com.kurly.android.commerce.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kurly.android.commerce.data.api.KurlyApi
import com.kurly.android.commerce.data.model.SectionProductsResponse
import com.kurly.android.commerce.data.paging.SectionPagingSource
import com.kurly.android.commerce.presentation.home.model.SectionUiModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KurlyRepository @Inject constructor(
    private val api: KurlyApi
) {
    fun getSectionsPaging(): Flow<PagingData<SectionUiModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SectionPagingSource(api) }
        ).flow

    }

    suspend fun getSectionProducts(sectionId: Int): SectionProductsResponse {
        return api.getSectionProducts(sectionId)
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}