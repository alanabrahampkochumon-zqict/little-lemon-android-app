package com.littlelemon.application.core.presentation.designsystem

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

data class ShadowSpec(
    val radius: Dp,
    val offset: DpOffset,
    val color: Int,
    val spread: Dp = 0.dp // FIXME: Add Blendmode
)

data class CustomShadow(
    val firstShadow: ShadowSpec,
    val secondShadow: ShadowSpec? = null
)

data class Shadows(
    // TODO: Change this to add px to dp helper
    val shadowUpperMD: CustomShadow = CustomShadow(
        ShadowSpec(radius = 8.dp, offset = DpOffset(x = 0.dp, y = (-2).dp), color = 0x0B000000),
        ShadowSpec(radius = 40.dp, offset = DpOffset(x = 0.dp, y = (-4).dp), color = 0x04000000)
    ),

    val upperXL: CustomShadow = CustomShadow(
        ShadowSpec(radius = (48f).dp, offset = DpOffset(x = 0.dp, y = (-8).dp), color = 0x04000000),
        ShadowSpec(radius = (24f).dp, offset = DpOffset(x = 0.dp, y = (-12).dp), color = 0x08000000)
    )
)


val LocalShadows = staticCompositionLocalOf { Shadows() }