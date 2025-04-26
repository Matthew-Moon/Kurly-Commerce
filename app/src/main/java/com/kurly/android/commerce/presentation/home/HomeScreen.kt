package com.kurly.android.commerce.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.kurly.android.commerce.presentation.common.KurlySection
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
    val favorites by viewModel.favorites.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = sections.loadState.refresh is LoadState.Loading,
        onRefresh = { sections.refresh() }
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
                    favorites = favorites,
                    onFavoriteClick = { productId ->
                        viewModel.toggleFavorite(productId)
                    }
                )
            }
        }

        PullRefreshIndicator(
            refreshing = sections.loadState.refresh is LoadState.Loading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = KurlyColor
        )

        // 추가 로딩 인디케이터
        if (sections.loadState.append is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = KurlyColor
            )
        }

        // 추가 로딩 에러
        if (sections.loadState.append is LoadState.Error) {
            val errorMessage = (sections.loadState.append as LoadState.Error).error.message
            Timber.e("추가 페이지 로드 실패: $errorMessage")
        }
    }
}

@Composable
private fun SectionList(
    sections: LazyPagingItems<SectionUiModel>,
    favorites: Set<Long>,
    onFavoriteClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            count = sections.itemCount,
            key = sections.itemKey { it.id }
        ) { index ->
            sections[index]?.let { section ->
                KurlySection(
                    model = section.copy(
                        products = section.products.map { product ->
                            product.copy(
                                isFavorite = favorites.contains(product.id)
                            )
                        }
                    ),
                    onFavoriteClick = onFavoriteClick
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}