package com.littlelemon.application.core.presentation.designsystem

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val sizeNone: Dp = 0.dp,
    val size2XS: Dp = 2.dp,
    val sizeXS: Dp = 4.dp,
    val sizeSM: Dp = 6.dp,
    val sizeMD: Dp = 8.dp,
    val sizeLG: Dp = 12.dp,
    val sizeXL: Dp = 16.dp,
    val size2XL: Dp = 24.dp,
    val size3XL: Dp = 32.dp,
    val size4XL: Dp = 40.dp,
    val size5XL: Dp = 48.dp,
    val size6XL: Dp = 64.dp,
    val size7XL: Dp = 80.dp,
)

val LocalDimensions = staticCompositionLocalOf { Dimens() }