package com.kurly.android.commerce.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.kurly.android.commerce.data.repository.KurlyRepository
import com.kurly.android.commerce.data.local.FavoritePreferences
import com.kurly.android.commerce.presentation.home.model.ProductUiModel
import com.kurly.android.commerce.presentation.home.model.SectionUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: KurlyRepository,
    private val favoritePreferences: FavoritePreferences
) : ViewModel() {

    private val _products = MutableStateFlow<List<ProductUiModel>>(emptyList())
    val products: StateFlow<List<ProductUiModel>> = _products.asStateFlow()

    // 섹션 페이징 데이터
    val sections: Flow<PagingData<SectionUiModel>> = repository.getSectionsPaging()
        .map { pagingData ->
            pagingData.map { section ->
                loadSectionProducts(section)
            }
        }
        .cachedIn(viewModelScope)

    fun toggleFavorite(productId: Long) {
        viewModelScope.launch {
            val currentProducts = _products.value
            val product = currentProducts.find { it.id == productId }
            val newFavoriteState = !(product?.isFavorite ?: false)

            // 먼저 UI 업데이트
            updateProductFavoriteStatus(productId, newFavoriteState)

            try {
                // 그 다음 DataStore에 저장
                favoritePreferences.setFavorite(productId, newFavoriteState)
                Timber.d("찜하기 상태 변경 성공: productId=$productId, isFavorite=$newFavoriteState")
            } catch (e: Exception) {
                // 저장 실패 시 UI 롤백
                updateProductFavoriteStatus(productId, !newFavoriteState)
                Timber.e(e, "찜하기 상태 변경 실패: productId=$productId")
            }
        }
    }

    private fun updateProductFavoriteStatus(productId: Long, isFavorite: Boolean) {
        val currentProducts = _products.value
        val updatedProducts = currentProducts.map { product ->
            if (product.id == productId) {
                product.copy(isFavorite = isFavorite)
            } else {
                product
            }
        }
        _products.value = updatedProducts

        // 디버그 로그 추가
        Timber.d("상품 찜하기 상태 업데이트: productId=$productId, isFavorite=$isFavorite")
        Timber.d("현재 찜한 상품 목록: ${updatedProducts.filter { it.isFavorite }.map { it.id }}")
    }

    private suspend fun loadSectionProducts(section: SectionUiModel): SectionUiModel {
        return try {
            val productsResponse = repository.getSectionProducts(section.id)
            Timber.d("섹션 ${section.id}의 상품 로드 완료: ${productsResponse.data.size}개")

            // 상품 목록 업데이트
            val newProducts = productsResponse.data.map { product ->
                ProductUiModel(
                    id = product.id,
                    name = product.name,
                    image = product.image,
                    originalPrice = product.originalPrice,
                    discountedPrice = product.discountedPrice,
                    isSoldOut = product.isSoldOut,
                    isFavorite = false
                )
            }

            // 전체 상품 목록 업데이트
            val currentProducts = _products.value.toMutableList()
            currentProducts.addAll(newProducts)
            _products.value = currentProducts.distinctBy { it.id }

            // 각 상품의 찜하기 상태 확인
            newProducts.forEach { product ->
                viewModelScope.launch {
                    favoritePreferences.getFavoriteStatus(product.id)
                        .collect { isFavorite ->
                            updateProductFavoriteStatus(product.id, isFavorite)
                        }
                }
            }

            section.copy(products = newProducts)
        } catch (e: Exception) {
            Timber.e(e, "섹션 ${section.id}의 상품 로드 실패")
            section
        }
    }
}
