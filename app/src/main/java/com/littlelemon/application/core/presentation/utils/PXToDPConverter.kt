package com.littlelemon.application.core.presentation.utils

import androidx.compose.ui.unit.Dp


fun Float.toDP(screenDensity: Float): Dp {
    return Dp(this / screenDensity)
}
