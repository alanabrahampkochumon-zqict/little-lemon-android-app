package com.littlelemon.application.core.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

@Composable
fun Modifier.shimmer(
    durationMillis: Int = 1000,
): Modifier {
    val transition = rememberInfiniteTransition(label = "")

    val colorStop by transition.animateFloat(
        initialValue = 0.0f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "color stop animation"
    )

    val colorStops = arrayOf(
        0.0f to LittleLemonTheme.colors.secondary,
        colorStop to LittleLemonTheme.colors.disabled,
        1.0f to LittleLemonTheme.colors.secondary
    )
    return drawBehind {
        drawRect(
            brush = Brush.linearGradient(
                colorStops = colorStops
            )
        )
    }
}


@Preview
@Composable
private fun ShimmerEffectPreview() {

    LittleLemonTheme {
        Box(Modifier
            .width(250.dp)
            .height(250.dp)
            .shimmer())
    }

}