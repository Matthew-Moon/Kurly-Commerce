package com.kurly.android.commerce.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kurly.android.commerce.data.api.KurlyApi
import com.kurly.android.commerce.presentation.home.model.SectionUiModel
import timber.log.Timber

class SectionPagingSource(
    private val api: KurlyApi
) : PagingSource<Int, SectionUiModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SectionUiModel> {
        return try {
            // 현재 페이지 번호 (null이면 1페이지)
            val page = params.key ?: 1
            Timber.d("섹션 페이지 로드 시작: page=$page, loadSize=${params.loadSize}")

            // API 호출
            val response = api.getSections(page)

            // 다음 페이지 계산
            val nextPage = when {
                response.data.isEmpty() -> {
                    Timber.d("더 이상 로드할 섹션이 없습니다: page=$page")
                    null
                }
                response.paging == null -> {
                    Timber.d("마지막 페이지입니다: page=$page")
                    null
                }
                else -> {
                    Timber.d("다음 페이지 존재: currentPage=$page, nextPage=${response.paging.nextPage}")
                    response.paging.nextPage
                }
            }

            // Section을 SectionUiModel로 변환
            val sections = response.data.map { section ->
                SectionUiModel(
                    id = section.id,
                    title = section.title,
                    type = section.type
                )
            }
            Timber.d("섹션 페이지 로드 완료: page=$page, sectionsCount=${sections.size}, sections=${sections.map { it.id }}")

            LoadResult.Page(
                data = sections,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextPage
            )
        } catch (e: Exception) {
//            Timber.e(e, "섹션 페이지 로드 실패: page=$page")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SectionUiModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
} 