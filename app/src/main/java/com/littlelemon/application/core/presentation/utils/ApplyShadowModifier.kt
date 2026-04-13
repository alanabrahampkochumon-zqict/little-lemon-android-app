package com.littlelemon.application.core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.ShadowLayers

@Composable
fun Modifier.applyShadow(shape: Shape, shadows: ShadowLayers): Modifier {
    return shadows.layers.fold(this) { modifier, shadowLayer ->
        modifier.dropShadow(
            shape = shape,
            shadow = Shadow(
                radius = shadowLayer.blurRadius.dp,
                spread = shadowLayer.spread.dp,
                offset = DpOffset(shadowLayer.xOffset.dp, shadowLayer.yOffset.dp),
                color = Color(shadowLayer.color).copy(alpha = shadowLayer.opacity)
            )
        )
    }
}