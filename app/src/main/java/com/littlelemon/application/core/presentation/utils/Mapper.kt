package com.littlelemon.application.core.presentation.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import com.littlelemon.application.core.presentation.designsystem.ShadowSpec

/**
 * Converts a user defined ShadowSpec(used in Design System) to a compose shadow.
 * @param screenDensity: Screen Density Ratio like 3.0, obtained from `displayMetrics`
 * @return Shadow object that can be used by dropShadow modifier.
 */
fun ShadowSpec.toComposeShadow(screenDensity: Float): Shadow {
    return Shadow(
        radius = this.radius.toDP(screenDensity),
        spread = this.spread.toDP(screenDensity),
        color = Color(this.color),
        offset = DpOffset(this.offsetX.toDP(screenDensity), this.offsetY.toDP(screenDensity)),
        alpha = alpha
    )
}

