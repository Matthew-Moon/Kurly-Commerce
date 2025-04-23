package com.kurly.android.commerce.presentation.common

fun Int.toPriceString(): String {
    return "%,d".format(this)
} 