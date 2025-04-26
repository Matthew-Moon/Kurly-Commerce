package com.kurly.android.commerce.presentation.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kurly.android.commerce.R
import com.kurly.android.commerce.presentation.home.model.ProductUiModel
import com.kurly.android.commerce.presentation.theme.*
import kotlinx.coroutines.delay

@Composable
private fun AnimatedFavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.2f else 1f,
        animationSpec = tween(
            durationMillis = 150,
            easing = FastOutSlowInEasing
        ), 
        label = "favorite_scale"
    )

    IconButton(
        onClick = {
            onClick()
            isPressed = true
        },
        modifier = modifier
            .padding(8.dp)
            .size(32.dp)
            .scale(scale)
    ) {
        Icon(
            painter = painterResource(
                id = if (isFavorite) R.drawable.ic_btn_heart_on
                else R.drawable.ic_btn_heart_off
            ),
            contentDescription = if (isFavorite) "찜하기 취소" else "찜하기",
            tint = Color.Unspecified
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

@Composable
fun KurlyProduct(
    modifier: Modifier = Modifier,
    product: ProductUiModel,
    isVertical: Boolean = false,
    onFavoriteClick: (Long) -> Unit = {}
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
        Box {
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
                                .aspectRatio(6f / 4f)
                        } else {
                            Modifier
                                .width(150.dp)
                                .height(200.dp)
                        }
                    )
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )

            AnimatedFavoriteButton(
                isFavorite = product.isFavorite,
                onClick = { onFavoriteClick(product.id) },
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }

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