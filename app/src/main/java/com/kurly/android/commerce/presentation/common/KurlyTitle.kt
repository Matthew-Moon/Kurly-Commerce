package com.kurly.android.commerce.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TitleTextStyle = TextStyle(
    fontSize = 16.sp,
    lineHeight = 20.sp,
    fontWeight = FontWeight.Bold,
    color = Color.Black
)

@Composable
fun KurlyTitle(
    title: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent // 기본 투명
) {
    Text(
        text = title,
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(12.dp),
        style = TitleTextStyle
    )
}

@Preview
@Composable
fun KurlyTitlePreview() {
    KurlyTitle(title = "함께하면 더 좋은 상품", backgroundColor = Color.White)
}
