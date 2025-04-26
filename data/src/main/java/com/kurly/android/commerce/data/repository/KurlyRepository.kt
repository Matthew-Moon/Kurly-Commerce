package com.kurly.android.commerce.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kurly.android.commerce.data.api.KurlyApi
import com.kurly.android.commerce.data.mapper.toProduct
import com.kurly.android.commerce.data.paging.SectionPagingSource
import com.kurly.android.commerce.domain.model.Product
import com.kurly.android.commerce.domain.model.Section
import com.kurly.android.commerce.domain.repository.KurlyRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class KurlyRepositoryImpl @Inject constructor(
    private val api: KurlyApi
) : KurlyRepository {
    override suspend fun getSectionProducts(sectionId: Int): List<Product> {
        return try {
            val response = api.getSectionProducts(sectionId)
            Timber.d("서버 응답: $response")
            response.data.map { it.toProduct() }
        } catch (e: Exception) {
            Timber.e(e, "섹션 상품 로드 실패: sectionId=$sectionId")
            emptyList()
        }
    }

    fun getSectionsPaging(pageSize: Int = DEFAULT_PAGE_SIZE): Flow<PagingData<Section>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SectionPagingSource(api) }
        ).flow
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 10
    }
}