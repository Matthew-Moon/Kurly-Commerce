package com.kurly.android.commerce.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kurly.android.commerce.ui.home.model.ProductUiModel
import com.kurly.android.commerce.ui.theme.CancelPrice
import com.kurly.android.commerce.ui.theme.DiscountLate
import com.kurly.android.commerce.ui.theme.ProductName
import com.kurly.android.commerce.ui.theme.ProductPrice

@Composable
fun KurlyProduct(
    modifier: Modifier = Modifier,
    product: ProductUiModel,
    isVertical: Boolean = false
) {
    Column(
        modifier = modifier
            .then(
                if (isVertical) {
                    Modifier.fillMaxWidth()
                } else {
                    Modifier.width(150.dp)
                }
            )
            .background(Color.White)
    ) {
        // 상품 이미지
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(product.image)
                .crossfade(true)
                .build(),
            contentDescription = product.name,
            modifier = Modifier
                .then(
                    if (isVertical) {
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(6f/4f) // 6:4 비율 설정
                    } else {
                        Modifier
                            .width(150.dp)
                            .height(200.dp)
                    }
                )
                .clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.Crop
        )

        // 상품명
        Text(
            text = product.name,
            maxLines = if (isVertical) 1 else 2,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Normal,
                color = ProductName
            ),
            modifier = Modifier.padding(top = 8.dp)
        )

        // 할인율 + 판매금액
        product.discountedPrice?.let { discountedPrice ->
            if (isVertical) {
                // vertical 타입일 때는 한 줄에 모든 가격 정보 표시
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 할인율
                    val discountRate = ((product.originalPrice - discountedPrice) * 100 / product.originalPrice)
                    Text(
                        text = "${discountRate}%",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DiscountLate
                        ),
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .alignByBaseline()
                    )

                    // 할인가
                    Text(
                        text = "${discountedPrice.toPriceString()}원",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ProductPrice
                        ), modifier = Modifier
                            .padding(end = 4.dp)
                            .alignByBaseline()
                    )

                    // 원가 (취소선)
                    Text(
                        text = "${product.originalPrice.toPriceString()}원",
                        style = TextStyle(
                            fontSize = 10.sp,
                            color = CancelPrice,
                            textDecoration = TextDecoration.LineThrough
                        ), modifier = Modifier
                            .padding(end = 4.dp)
                            .alignByBaseline()
                    )
                }
            } else {
                // 기존 레이아웃 (할인율과 할인가 한 줄, 원가 다음 줄)
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 할인율
                    val discountRate = ((product.originalPrice - discountedPrice) * 100 / product.originalPrice)
                    Text(
                        text = "${discountRate}%",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DiscountLate
                        ),
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .alignByBaseline()
                    )

                    // 할인가
                    Text(
                        text = "${discountedPrice.toPriceString()}원",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ProductPrice
                        ), modifier = Modifier
                            .padding(end = 4.dp)
                            .alignByBaseline()
                    )
                }

                // 원가 (취소선)
                Text(
                    text = "${product.originalPrice.toPriceString()}원",
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = CancelPrice,
                        textDecoration = TextDecoration.LineThrough
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        } ?: run {
            // 할인이 없는 경우 원가만 표시
            Text(
                text = "${product.originalPrice.toPriceString()}원",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ProductPrice
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview
@Composable
fun KurlyProductPreview() {
    KurlyProduct(
        product = ProductUiModel(
            id = 1,
            name = "[샐러딩] 레디믹스 스탠다드 150g\n가나다라",
            image = "",
            originalPrice = 6200,
            discountedPrice = 5200,
            isSoldOut = false
        ), isVertical = true
    )
}