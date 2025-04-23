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

@Composable
fun KurlyTitle(modifier: Modifier = Modifier, title: String) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(Color.White),
        text = title,
        style = TextStyle(
            fontSize = 16.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        ),
    )
}

@Preview
@Composable
fun KurlyTitlePreview() {
    KurlyTitle(title = "함께하면 더 좋은 상품")
}