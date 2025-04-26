package com.kurly.android.commerce.data.usecase

import androidx.paging.PagingData
import com.kurly.android.commerce.data.repository.KurlyRepositoryImpl
import com.kurly.android.commerce.domain.model.Section
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 페이징은 안드로이드 라이브러리 의존성이므로 도메인 레이어가 아닌 데이터 레이어에 위치
 */
class GetSectionsPagingUseCase @Inject constructor(
    private val repository: KurlyRepositoryImpl
) {
    operator fun invoke(pageSize: Int = DEFAULT_PAGE_SIZE): Flow<PagingData<Section>> {
        return repository.getSectionsPaging(pageSize)
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 10
    }
}