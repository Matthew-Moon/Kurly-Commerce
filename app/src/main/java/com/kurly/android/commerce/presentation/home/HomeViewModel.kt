package com.kurly.android.commerce.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kurly.android.commerce.data.repository.KurlyRepository
import com.kurly.android.commerce.presentation.home.model.ProductUiModel
import com.kurly.android.commerce.presentation.home.model.SectionUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: KurlyRepository
) : ViewModel() {

    private val _sections = MutableStateFlow<List<SectionUiModel>>(emptyList())
    val sections: StateFlow<List<SectionUiModel>> = _sections.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // 찜한 상품 ID 목록
    private val _favorites = MutableStateFlow<Set<Long>>(emptySet())
    val favorites: StateFlow<Set<Long>> = _favorites.asStateFlow()

    init {
        loadSections()
    }

    fun toggleFavorite(productId: Long) {
        val currentFavorites = _favorites.value.toMutableSet()
        if (currentFavorites.contains(productId)) {
            currentFavorites.remove(productId)
        } else {
            currentFavorites.add(productId)
        }
        _favorites.value = currentFavorites
        
        // TODO: API 연동 시 서버에 찜하기 상태 업데이트
        Timber.d("찜하기 상태 변경: productId=$productId, isFavorite=${currentFavorites.contains(productId)}")
    }

    private fun loadSections() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                // 1. 섹션 목록 로드
                val sectionsResponse = repository.getSections(1)
                Timber.d("섹션 로드 완료: ${sectionsResponse.data.size}개")

                // 2. 각 섹션을 UI 모델로 변환하고 임시 저장
                val sectionUiModels = sectionsResponse.data.map { section ->
                    SectionUiModel(
                        id = section.id,
                        title = section.title,
                        type = section.type
                    )
                }
                _sections.value = sectionUiModels

                // 3. 각 섹션의 상품 정보를 순차적으로 로드
                sectionsResponse.data.forEach { section ->
                    try {
                        val productsResponse = repository.getSectionProducts(section.id)
                        Timber.d("섹션 ${section.id}의 상품 로드 완료: ${productsResponse.data.size}개")

                        // 4. 상품 정보를 포함하여 섹션 UI 모델 업데이트
                        val updatedSections = _sections.value.map { uiSection ->
                            if (uiSection.id == section.id) {
                                uiSection.copy(
                                    products = productsResponse.data.map { product ->
                                        ProductUiModel(
                                            id = product.id,
                                            name = product.name,
                                            image = product.image,
                                            originalPrice = product.originalPrice,
                                            discountedPrice = product.discountedPrice,
                                            isSoldOut = product.isSoldOut
                                        )
                                    }
                                )
                            } else {
                                uiSection
                            }
                        }
                        _sections.value = updatedSections
                    } catch (e: Exception) {
                        Timber.e(e, "섹션 ${section.id}의 상품 로드 실패")
                        _error.value = "일부 상품을 불러오는데 실패했습니다."
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "섹션 목록 로드 실패")
                _error.value = "섹션 목록을 불러오는데 실패했습니다."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        Timber.d("새로고침 시작")
        _sections.value = emptyList() // 기존 데이터 초기화
        loadSections() // 전체 데이터 다시 로드
    }

}
