package com.kurly.android.commerce.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.kurly.android.commerce.data.local.FavoritePreferences
import com.kurly.android.commerce.data.usecase.GetSectionsPagingUseCase
import com.kurly.android.commerce.domain.usecase.GetSectionProductsUseCase
import com.kurly.android.commerce.presentation.home.model.ProductUiModel
import com.kurly.android.commerce.presentation.home.model.SectionUiModel
import com.kurly.android.commerce.presentation.home.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSectionProductsUseCase: GetSectionProductsUseCase,
    getSectionsPagingUseCase: GetSectionsPagingUseCase,
    private val favoritePreferences: FavoritePreferences
) : ViewModel() {

    // 현재 표시된 섹션의 상품 목록
    private val _products = MutableStateFlow<Map<Int, List<ProductUiModel>>>(emptyMap())
    val products: StateFlow<Map<Int, List<ProductUiModel>>> = _products.asStateFlow()
    
    // 현재 로드 중인 섹션들
    private val _loadingSections = MutableStateFlow<Set<Int>>(emptySet())
    val loadingSections: StateFlow<Set<Int>> = _loadingSections.asStateFlow()

    // 현재 선택된 섹션 ID
    private val _selectedSectionId = MutableStateFlow<Int?>(null)
    val selectedSectionId: StateFlow<Int?> = _selectedSectionId.asStateFlow()

    // 새로고침 중인지 상태
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    // 페이징을 사용한 섹션 목록 로드
    val sections: Flow<PagingData<SectionUiModel>> = getSectionsPagingUseCase()
        .map { pagingData -> 
            pagingData.map { section -> section.toUiModel() }
        }
        .cachedIn(viewModelScope)

    /**
     * 특정 섹션을 선택하고 해당 섹션의 상품을 로드합니다.
     */
    fun selectSection(sectionId: Int) {
        _selectedSectionId.value = sectionId
        
        // 아직 해당 섹션의 상품이 로드되지 않았으면 로드
        if (!_products.value.containsKey(sectionId)) {
            loadSectionProducts(sectionId)
        }
    }
    
    /**
     * 특정 섹션의 상품 목록을 로드합니다.
     */
    private fun loadSectionProducts(sectionId: Int) {
        // 이미 로딩 중이거나 로드된 섹션은 중복 로드 방지
        if (_loadingSections.value.contains(sectionId) || _products.value.containsKey(sectionId)) {
            return
        }
        
        // 로딩 상태 업데이트
        _loadingSections.update { it + sectionId }
        
        viewModelScope.launch {
            try {
                // 해당 섹션의 상품 로드
                val productList = getSectionProductsUseCase(sectionId)
                val productUiModels = productList.map { 
                    it.toUiModel(
                        isFavorite = favoritePreferences.isFavorite(it.id), 
                        sectionId = sectionId
                    ) 
                }
                
                // 상품 목록 업데이트 (기존 맵에 추가)
                _products.update { currentMap ->
                    currentMap + (sectionId to productUiModels)
                }
                
                Timber.d("섹션 상품 로드 완료: sectionId=$sectionId, count=${productUiModels.size}")
            } catch (e: Exception) {
                Timber.e(e, "섹션 상품 로드 실패: sectionId=$sectionId")
            } finally {
                // 로딩 상태 제거
                _loadingSections.update { it - sectionId }
            }
        }
    }
    
    /**
     * 화면에 표시된 섹션들의 상품을 로드합니다.
     */
    fun loadVisibleSections(visibleSectionIds: List<Int>) {
        visibleSectionIds.forEach { sectionId ->
            if (!_products.value.containsKey(sectionId) && !_loadingSections.value.contains(sectionId)) {
                loadSectionProducts(sectionId)
            }
        }
    }

    /**
     * 새로고침 수행 - 모든 상품 데이터를 초기화하고 다시 로드합니다.
     */
    fun refresh() {
        viewModelScope.launch {
            try {
                _isRefreshing.value = true
                // 모든 상품 데이터 초기화
                _products.value = emptyMap()
                // 로딩 상태 초기화
                _loadingSections.value = emptySet()
                
                // 현재 선택된 섹션이 있다면 해당 섹션의 상품을 다시 로드
                _selectedSectionId.value?.let { sectionId ->
                    loadSectionProducts(sectionId)
                }
            } catch (e: Exception) {
                Timber.e(e, "새로고침 실패")
            } finally {
                // 새로고침 완료
                _isRefreshing.value = false
            }
        }
    }

    /**
     * 상품의 찜하기 상태를 토글합니다.
     */
    fun toggleFavorite(productId: Long, sectionId: Int) {
        viewModelScope.launch {
            // 해당 섹션의 상품 목록 가져오기
            val sectionProducts = _products.value[sectionId] ?: return@launch
            
            // 상품 찾기
            val product = sectionProducts.find { it.id == productId } ?: return@launch
            val newFavoriteState = !product.isFavorite

            // UI 업데이트
            updateProductFavoriteStatus(productId, newFavoriteState, sectionId)

            try {
                // DataStore에 저장
                favoritePreferences.setFavorite(productId, newFavoriteState)
                Timber.d("찜하기 상태 변경 성공: productId=$productId, isFavorite=$newFavoriteState")
            } catch (e: Exception) {
                // 저장 실패 시 UI 롤백
                updateProductFavoriteStatus(productId, !newFavoriteState, sectionId)
                Timber.e(e, "찜하기 상태 변경 실패: productId=$productId")
            }
        }
    }

    /**
     * 상품의 찜하기 상태를 업데이트합니다.
     */
    private fun updateProductFavoriteStatus(productId: Long, isFavorite: Boolean, sectionId: Int) {
        val sectionProducts = _products.value[sectionId] ?: return
        
        // 해당 섹션의 상품 목록 업데이트
        val updatedProducts = sectionProducts.map { product ->
            if (product.id == productId) {
                product.copy(isFavorite = isFavorite)
            } else {
                product
            }
        }
        
        // 상품 맵 업데이트
        _products.update { currentMap ->
            currentMap + (sectionId to updatedProducts)
        }
    }
}

