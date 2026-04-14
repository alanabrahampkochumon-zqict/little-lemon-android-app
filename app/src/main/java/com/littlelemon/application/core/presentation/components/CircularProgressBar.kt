package com.littlelemon.application.core.presentation.components

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    brush: Brush = LittleLemonTheme.gradients.primaryBrand,
    strokeWidth: Dp = 4.dp,
    startAngle: Float = 45f,
    endAngle: Float = 180f,
    indefinite: Boolean = false
) {
    val targetAngle = 240f
    val startAngle = 60f
    val progress = if (indefinite) {
        val infiniteTransition = rememberInfiniteTransition(label = "progress transition")

        infiniteTransition.animateFloat(
            initialValue = startAngle,
            targetValue = targetAngle,
            animationSpec = infiniteRepeatable(
                tween(2500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "progress"
        )
    } else {
        remember { mutableFloatStateOf(endAngle) }
    }

    val start = if (indefinite) {
        val infiniteTransition = rememberInfiniteTransition(label = "progress transition")

        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                tween(2500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ), label = "progress"
        )
    } else {
        remember { mutableFloatStateOf(startAngle) }
    }
    val minProgressSize = 24.dp
    Canvas(
        modifier = modifier
            .sizeIn(minHeight = minProgressSize, minWidth = minProgressSize)
            .size(40.dp)
    ) {
        drawArc(
            brush,
            startAngle = start.value,
            sweepAngle = progress.value,
            useCenter = false,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CircularProgressBarPreview() {
    LittleLemonTheme {
        Column(Modifier.fillMaxSize()) {
            CircularProgressBar(indefinite = true)
            CircularProgressBar(indefinite = false)
        }
    }
}