package com.kurly.android.commerce.presentation.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.kurly.android.commerce.presentation.theme.CancelPrice
import com.kurly.android.commerce.presentation.theme.DiscountLate
import com.kurly.android.commerce.presentation.theme.ProductName
import com.kurly.android.commerce.presentation.theme.ProductPrice
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import timber.log.Timber

/**
 * ripple 효과가 없는 InteractionSource 구현
 */
class NoRippleInteractionSource : MutableInteractionSource {
    override val interactions = emptyFlow<androidx.compose.foundation.interaction.Interaction>()
    override suspend fun emit(interaction: androidx.compose.foundation.interaction.Interaction) {}
    override fun tryEmit(interaction: androidx.compose.foundation.interaction.Interaction) = true
}

/**
 * ripple 효과 없는 InteractionSource를 기억하는 composable 함수
 */
@Composable
fun rememberNoRippleInteractionSource() = remember { NoRippleInteractionSource() }

@Composable
private fun AnimatedFavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.2f else 1f,
        animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing),
        label = "favorite_scale"
    )

    IconButton(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = modifier
            .padding(8.dp)
            .size(32.dp)
            .scale(scale),
        interactionSource = rememberNoRippleInteractionSource()
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

    LaunchedEffect(isPressed == true) {
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
    // ProductUiModel에 추가된 할인율 계산 함수 사용
    val discountRate = product.getDiscountRate()
    val hasDiscount = product.hasDiscount()

    Timber.d("상품 정보: ${product.name}, 원가: ${product.originalPrice}, 할인가: ${product.discountedPrice}, 할인율: $discountRate%")

    val discountTextStyle = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = DiscountLate
    )

    val discountedPriceTextStyle = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = ProductPrice
    )

    val originalPriceTextStyle = TextStyle(
        fontSize = 10.sp,
        color = CancelPrice,
        textDecoration = TextDecoration.LineThrough
    )

    Column(
        modifier = modifier
            .background(Color.White)
            .then(
                if (isVertical) Modifier.fillMaxWidth()
                else Modifier.width(150.dp)
            )
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.image)
                    .crossfade(true)
                    .build(),
                contentDescription = product.name,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .then(
                        if (isVertical) Modifier
                            .fillMaxWidth()
                            .aspectRatio(6f / 4f)
                        else Modifier
                            .width(150.dp)
                            .height(200.dp)
                    ),
                contentScale = ContentScale.Crop
            )

            AnimatedFavoriteButton(
                isFavorite = product.isFavorite,
                onClick = { onFavoriteClick(product.id) },
                modifier = Modifier.align(Alignment.TopEnd)
            )

            if (product.isSoldOut) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "품절",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }

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

        if (hasDiscount) {
            if (isVertical) {
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$discountRate%",
                        style = discountTextStyle,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .alignByBaseline()
                    )

                    Text(
                        text = "${product.discountedPrice?.toPriceString()}원",
                        style = discountedPriceTextStyle,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .alignByBaseline()
                    )

                    Text(
                        text = "${product.originalPrice.toPriceString()}원",
                        style = originalPriceTextStyle,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .alignByBaseline()
                    )
                }
            } else {
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$discountRate%",
                        style = discountTextStyle,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .alignByBaseline()
                    )

                    Text(
                        text = "${product.discountedPrice?.toPriceString()}원",
                        style = discountedPriceTextStyle,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .alignByBaseline()
                    )
                }

                Text(
                    text = "${product.originalPrice.toPriceString()}원",
                    style = originalPriceTextStyle,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        } else {
            Text(
                text = "${product.originalPrice.toPriceString()}원",
                style = discountedPriceTextStyle,
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
            name = "[샐러딩] 레디믹스 스탠다드 150g\n가나다라마바사",
            image = "",
            originalPrice = 6200,
            discountedPrice = 5200,
            isSoldOut = false,
            sectionId = 1
        ),
        isVertical = true
    )
}
