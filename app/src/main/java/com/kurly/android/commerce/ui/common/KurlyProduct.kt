package com.kurly.android.commerce.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun KurlyProduct(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .width(150.dp)
            .background(Color.White)
    ) {
        Box(
            modifier = modifier
                .height(150.dp)
                .fillMaxWidth()
                .background(Color.Red)
        ) {

        }

        Text(
            modifier = modifier.padding(5.dp),
            text = "[샐러딩] 레디믹스 스탠다드 150g",
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.W300,
                color = Color.Black
            ),
        )

        Row(
            modifier.padding(start = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = modifier
                    .padding(end = 5.dp)
                    .alignByBaseline(),
                text = "30%",
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.W700,
                    color = Color.Black
                ),
            )

            Text(
                modifier = modifier
                    .alignByBaseline(),
                text = "6,200원",
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.W700,
                    color = Color.Black
                ),
            )

        }

        Text(
            modifier = modifier.padding(start = 5.dp),
            text = "5,200원",
            style = TextStyle(
                fontSize = 10.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.W400,
                color = Color.Gray,
                textDecoration = TextDecoration.LineThrough
            ),
        )

    }
}

@Preview
@Composable
fun KurlyProductPreview() {
    KurlyProduct()
}