package com.littlelemon.application.core.presentation.components

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

@Composable
fun IndefiniteCircularProgressIndicator(
    modifier: Modifier = Modifier,
    brush: Brush = LittleLemonTheme.gradients.primaryBrand,
    strokeWidth: Dp = 4.dp,
) {
    val targetAngle = 240f
    val startAngle = 60f
    val animationDuration = 2400
    val minProgressSize = 24.dp

    val infiniteTransition = rememberInfiniteTransition(label = "progress transition")

    // Sweep angle for animating the progress
    val progress by infiniteTransition.animateFloat(
        initialValue = startAngle,
        targetValue = targetAngle,
        animationSpec = infiniteRepeatable(
            tween(animationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "progress"
    )

    // Start angle for rotating the progress bar
    val start by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(animationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "progress"
    )

    Canvas(
        modifier = modifier
            .sizeIn(minHeight = minProgressSize, minWidth = minProgressSize)
            .size(40.dp)
    ) {
        drawArc(
            brush,
            startAngle = start,
            sweepAngle = progress,
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
            IndefiniteCircularProgressIndicator(modifier = Modifier.size(100.dp))
        }
    }
}