package com.kurly.android.commerce.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kurly.android.commerce.data.api.KurlyApi
import com.kurly.android.commerce.data.mapper.toSection
import com.kurly.android.commerce.domain.model.Section
import retrofit2.HttpException
import java.io.IOException
import timber.log.Timber

class SectionPagingSource(
    private val api: KurlyApi
) : PagingSource<Int, Section>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Section> {
        val page = params.key ?: 1
        
        return try {
            // API 호출
            val response = api.getSections(page)
            val sections = response.data.map { it.toSection() }

            // 다음 페이지 계산
            val nextKey = when {
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


            Timber.d("섹션 페이지 로드 완료: page=$page, sectionsCount=${sections.size}")
            
            LoadResult.Page(
                data = sections,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            Timber.e(e, "섹션 페이지 로드 실패: 네트워크 오류")
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Timber.e(e, "섹션 페이지 로드 실패: HTTP 오류 ${e.code()}")
            LoadResult.Error(e)
        } catch (e: Exception) {
            Timber.e(e, "섹션 페이지 로드 실패: 기타 오류")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Section>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
} 