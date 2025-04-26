package com.kurly.android.commerce.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.kurly.android.commerce.presentation.common.KurlySection
import com.kurly.android.commerce.presentation.home.model.ProductUiModel
import com.kurly.android.commerce.presentation.home.model.SectionUiModel
import com.kurly.android.commerce.presentation.theme.KurlyColor
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val sections = viewModel.sections.collectAsLazyPagingItems()
    val productsMap by viewModel.products.collectAsState()
    val selectedSectionId by viewModel.selectedSectionId.collectAsState()
    val loadingSections by viewModel.loadingSections.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    
    val listState = rememberLazyListState()
    
    // 현재 화면에 보이는 섹션 ID 목록 계산
    val visibleSectionIds = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            visibleItems.mapNotNull { itemInfo ->
                val index = itemInfo.index
                if (index >= 0 && index < sections.itemCount) {
                    sections[index]?.id
                } else {
                    null
                }
            }
        }
    }
    
    // 화면에 보이는 섹션들의 상품 로드
    LaunchedEffect(visibleSectionIds.value) {
        if (visibleSectionIds.value.isNotEmpty()) {
            viewModel.loadVisibleSections(visibleSectionIds.value)
        }
    }
    
    // 첫 번째 섹션 선택
    LaunchedEffect(sections.itemCount) {
        if (sections.itemCount > 0 && selectedSectionId == null) {
            sections[0]?.id?.let { firstSectionId ->
                viewModel.selectSection(firstSectionId)
            }
        }
    }

    // Pull-to-Refresh 상태 관리
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing || sections.loadState.refresh is LoadState.Loading,
        onRefresh = { 
            // 뷰모델의 refresh 함수 호출 후 페이징 데이터도 새로고침
            viewModel.refresh() 
            sections.refresh() 
        }
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .pullRefresh(pullRefreshState)
    ) {
        when {
            sections.loadState.refresh is LoadState.Error -> {
                val errorMessage = (sections.loadState.refresh as LoadState.Error).error.message
                Text(
                    text = errorMessage ?: "오류가 발생했습니다.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            sections.itemCount > 0 -> {
                SectionList(
                    sections = sections,
                    productsMap = productsMap,
                    loadingSections = loadingSections,
                    onFavoriteClick = { productId, sectionId ->
                        viewModel.toggleFavorite(productId, sectionId)
                    },
                    listState = listState
                )
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing || sections.loadState.refresh is LoadState.Loading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = KurlyColor
        )

        if (sections.loadState.append is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = KurlyColor
            )
        }

        if (sections.loadState.append is LoadState.Error) {
            val errorMessage = (sections.loadState.append as LoadState.Error).error.message
            Timber.e("추가 페이지 로드 실패: $errorMessage")
        }
    }
}

@Composable
private fun SectionList(
    sections: LazyPagingItems<SectionUiModel>,
    productsMap: Map<Int, List<ProductUiModel>>,
    loadingSections: Set<Int>,
    onFavoriteClick: (Long, Int) -> Unit,
    listState: LazyListState
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState
    ) {
        items(
            count = sections.itemCount,
            key = sections.itemKey { it.id }
        ) { index ->
            sections[index]?.let { section ->
                val sectionId = section.id
                val isLoading = loadingSections.contains(sectionId)
                val sectionProducts = productsMap[sectionId] ?: emptyList()
                
                KurlySection(
                    model = section,
                    products = sectionProducts,
                    isLoading = isLoading,
                    onFavoriteClick = { productId -> onFavoriteClick(productId, sectionId) },
                )

                if (index < sections.itemCount - 1) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        thickness = 3.dp,
                        color = KurlyColor
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
