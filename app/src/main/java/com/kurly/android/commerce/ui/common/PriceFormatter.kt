package com.kurly.android.commerce.ui.common

fun Int.toPriceString(): String {
    return "%,d".format(this)
} 