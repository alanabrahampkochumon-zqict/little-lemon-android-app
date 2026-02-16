package com.littlelemon.application.core.presentation.designsystem

import androidx.compose.runtime.staticCompositionLocalOf

data class ShadowSpec(
    val radius: Float,
    val offsetX: Float,
    val offsetY: Float,
    val color: Int,
    val spread: Float = 0f,
    val alpha: Float = 1.0f,
    // FIXME: Add Blendmode
)

data class CustomShadow(
    val firstShadow: ShadowSpec,
    val secondShadow: ShadowSpec? = null
)

data class Shadows(
    val shadowUpperMD: CustomShadow = CustomShadow(
        ShadowSpec(radius = 8f, offsetX = 0f, offsetY = -2f, color = 0x000000, alpha = 0.12f),
        ShadowSpec(radius = 40f, offsetX = 0f, offsetY = -4f, color = 0x000000, alpha = 0.04f)
    ),

    val upperXL: CustomShadow = CustomShadow(
        ShadowSpec(radius = 48f, offsetX = 0f, offsetY = -8f, color = 0x000000, alpha = 0.04f),
        ShadowSpec(radius = 24f, offsetX = 0f, offsetY = -12f, color = 0x000000, alpha = 0.08f)
    )
)


val LocalShadows = staticCompositionLocalOf { Shadows() }