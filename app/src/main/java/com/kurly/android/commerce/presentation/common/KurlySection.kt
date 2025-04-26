package com.kurly.android.commerce.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kurly.android.commerce.presentation.home.model.ProductUiModel
import com.kurly.android.commerce.presentation.home.model.SectionType
import com.kurly.android.commerce.presentation.home.model.SectionUiModel
import com.kurly.android.commerce.presentation.theme.KurlyColor
import timber.log.Timber

@Composable
fun KurlySection(
    model: SectionUiModel,
    products: List<ProductUiModel>,
    isLoading: Boolean = false,
    onFavoriteClick: (Long) -> Unit = {},
) {
    Timber.d("섹션 표시: ${model.title}, 상품 수: ${products.size}, 로딩 상태: $isLoading")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // 섹션 제목
        KurlyTitle(title = model.title)

        Spacer(modifier = Modifier.height(5.dp))

        // 상품 또는 로딩 표시
        if (isLoading) {
            // 로딩 인디케이터 표시
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = KurlyColor
                )
            }
        } else if (products.isEmpty()) {
            // 상품이 없는 경우 메시지 표시
            Text(
                text = "상품을 불러오는 중...",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                textAlign = TextAlign.Center
            )
        } else {
            // 섹션 타입에 따른 레이아웃
            when (model.type) {
                SectionType.HORIZONTAL -> HorizontalProductList(
                    products = products,
                    onFavoriteClick = onFavoriteClick
                )

                SectionType.VERTICAL -> VerticalProductList(
                    products = products,
                    onFavoriteClick = onFavoriteClick
                )

                SectionType.GRID -> GridProductList(
                    products = products,
                    onFavoriteClick = onFavoriteClick
                )

                else -> HorizontalProductList(
                    products = products,
                    onFavoriteClick = onFavoriteClick
                )
            }
        }
    }
}

@Composable
private fun GridProductList(
    products: List<ProductUiModel>,
    onFavoriteClick: (Long) -> Unit = {}
) {
    LazyHorizontalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .padding(horizontal = 12.dp),
        rows = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(products) { product ->
            KurlyProduct(
                product = product,
                onFavoriteClick = { onFavoriteClick(product.id) }
            )
        }
    }
}

@Composable
private fun HorizontalProductList(
    products: List<ProductUiModel>,
    onFavoriteClick: (Long) -> Unit = {}
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(end = 8.dp)
    ) {
        items(products) { product ->
            KurlyProduct(
                product = product,
                onFavoriteClick = { onFavoriteClick(product.id) }
            )
        }
    }
}

@Composable
private fun VerticalProductList(
    products: List<ProductUiModel>,
    onFavoriteClick: (Long) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        products.forEach { product ->
            KurlyProduct(
                product = product,
                modifier = Modifier.fillMaxWidth(),
                isVertical = true,
                onFavoriteClick = { onFavoriteClick(product.id) }
            )
        }
    }
}

@Preview
@Composable
fun KurlySectionPreview() {
    val section = SectionUiModel(
        id = 1,
        title = "컬리의 추천",
        type = SectionType.HORIZONTAL
    )

    val products = listOf(
        ProductUiModel(
            id = 1,
            name = "맛있는 사과",
            image = "",
            originalPrice = 6000,
            discountedPrice = 5000,
            isSoldOut = false,
            sectionId = 1
        ),
        ProductUiModel(
            id = 2,
            name = "신선한 배",
            image = "",
            originalPrice = 7000,
            discountedPrice = null,
            isSoldOut = false,
            sectionId = 1
        )
    )

    KurlySection(
        model = section,
        products = products,
        onFavoriteClick = {},
    )
}

