package com.kurly.android.commerce.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kurly.android.commerce.presentation.home.model.ProductUiModel
import com.kurly.android.commerce.presentation.home.model.SectionType
import com.kurly.android.commerce.presentation.home.model.SectionUiModel
import com.kurly.android.commerce.presentation.theme.KurlyColor

@Composable
fun KurlySection(
    model: SectionUiModel,
    onFavoriteClick: (Long) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // 섹션 제목
        KurlyTitle(title = model.title)

        Spacer(modifier = Modifier.height(5.dp))

        // 섹션 타입에 따른 레이아웃
        when (model.type) {
            SectionType.HORIZONTAL -> HorizontalProductList(products = model.products, onFavoriteClick = onFavoriteClick)
            SectionType.VERTICAL -> VerticalProductList(products = model.products, onFavoriteClick = onFavoriteClick)
            SectionType.GRID -> GridProductList(products = model.products, onFavoriteClick = onFavoriteClick)
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

