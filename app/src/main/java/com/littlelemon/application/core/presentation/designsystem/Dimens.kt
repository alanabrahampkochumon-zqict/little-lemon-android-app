package com.littlelemon.application.core.presentation.designsystem

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val spacingNone: Dp = 0.dp,
    val spacing2XS: Dp = 2.dp,
    val spacingXS: Dp = 4.dp,
    val spacingSM: Dp = 6.dp,
    val spacingMD: Dp = 8.dp,
    val spacingLG: Dp = 12.dp,
    val spacingXL: Dp = 16.dp,
    val spacing2XL: Dp = 24.dp,
    val spacing3XL: Dp = 32.dp,
    val spacing4XL: Dp = 40.dp,
    val spacing5XL: Dp = 48.dp,
    val spacing6XL: Dp = 64.dp,
    val spacing7XL: Dp = 80.dp,
)

val LocalDimensions = staticCompositionLocalOf { Dimens() }