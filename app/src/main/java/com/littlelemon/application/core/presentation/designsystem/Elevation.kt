package com.littlelemon.application.core.presentation.designsystem

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ShadowElevation(
    val small: Dp = 2.dp,
    val medium: Dp = 4.dp,
    val large: Dp = 8.dp,
    val xLarge: Dp = 16.dp
)

val LocalShadowElevation = staticCompositionLocalOf { ShadowElevation() }