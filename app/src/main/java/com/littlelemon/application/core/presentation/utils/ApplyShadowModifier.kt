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
fun Modifier.applyShadow(shape: Shape, shadows: ShadowLayers) {
    shadows.layers.forEach { layer ->
        this.dropShadow(
            shape,
            Shadow(
                radius = layer.blurRadius.dp,
                spread = layer.spread.dp,
                offset = DpOffset(
                    layer.xOffset.dp,
                    layer.yOffset.dp
                ),
                color = Color(layer.color),
                alpha = layer.opacity
            )
        )
    }
}