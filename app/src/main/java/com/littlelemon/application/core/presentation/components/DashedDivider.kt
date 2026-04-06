package com.littlelemon.application.core.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.colors

@Composable
fun DashedDivider(modifier: Modifier = Modifier) {
    val color = MaterialTheme.colors.outlineSecondary
    Canvas(
        modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12.dp.toPx(), 7.dp.toPx()), 0f),
            strokeWidth = 1.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}