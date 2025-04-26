package com.kurly.android.commerce.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.kurly.android.commerce.data.repository.KurlyRepository
import com.kurly.android.commerce.presentation.home.model.ProductUiModel
import com.kurly.android.commerce.presentation.home.model.SectionUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: KurlyRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // 찜한 상품 ID 목록
    private val _favorites = MutableStateFlow<Set<Long>>(emptySet())
    val favorites: StateFlow<Set<Long>> = _favorites.asStateFlow()

    // 섹션 페이징 데이터
    val sections: Flow<PagingData<SectionUiModel>> = repository.getSectionsPaging()
        .map { pagingData ->
            pagingData.map { section ->
                loadSectionProducts(section)
            }
        }
        .cachedIn(viewModelScope)

    fun toggleFavorite(productId: Long) {
        val currentFavorites = _favorites.value.toMutableSet()
        if (currentFavorites.contains(productId)) {
            currentFavorites.remove(productId)
        } else {
            currentFavorites.add(productId)
        }
        _favorites.value = currentFavorites
        Timber.d("찜하기 상태 변경: productId=$productId, isFavorite=${currentFavorites.contains(productId)}")
    }

    private suspend fun loadSectionProducts(section: SectionUiModel): SectionUiModel {
        return try {
            val productsResponse = repository.getSectionProducts(section.id)
            Timber.d("섹션 ${section.id}의 상품 로드 완료: ${productsResponse.data.size}개")

            section.copy(
                products = productsResponse.data.map { product ->
                    ProductUiModel(
                        id = product.id,
                        name = product.name,
                        image = product.image,
                        originalPrice = product.originalPrice,
                        discountedPrice = product.discountedPrice,
                        isSoldOut = product.isSoldOut,
                        isFavorite = _favorites.value.contains(product.id)
                    )
                }
            )
        } catch (e: Exception) {
            Timber.e(e, "섹션 ${section.id}의 상품 로드 실패")
            section
        }
    }
}
