package com.littlelemon.application.core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import com.littlelemon.application.core.presentation.designsystem.ShadowSpec

@Composable
fun ShadowSpec.toComposeShadow(): Shadow {
    return Shadow(
        radius = this.radius,
        spread = this.spread,
        color = Color(this.color),
        offset = this.offset
    )
}

