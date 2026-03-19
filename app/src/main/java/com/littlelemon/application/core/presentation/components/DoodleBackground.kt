package com.littlelemon.application.core.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import kotlin.math.max
import kotlin.random.Random

@Composable
fun DoodleBackground(modifier: Modifier = Modifier, alpha: Float = 0.15f) {
    val drawables = remember { mutableListOf<Int>() }
    try {
        val drawableClass = R.drawable::class.java
        val fields = drawableClass.fields

        for (field in fields) {
            if (field.name.startsWith("bg_icon_"))
                drawables.add(field.getInt(null))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    // TODO: Update to cache all the calculated values
    val painters = drawables.map { rememberVectorPainter(ImageVector.vectorResource(it)) }
    Canvas(
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colors.secondary)
    ) {
        val (width, height) = size
        var remainingWidth = width
        while (remainingWidth > 0) {
            var offset = 0.0f
            var remainingH = height

            while (remainingH > 0.0f) {
                val painter = painters.random()
                val (pWidth, pHeight) = painter.intrinsicSize
                val x = width - remainingWidth
                val y = height - remainingH
                val scale = Random.nextDouble(0.75, 0.95).toFloat()
                val drawHeight = pHeight * scale
                val drawWidth = pHeight * scale
                translate(x, y) {
                    rotate(
                        Random.nextFloat() * 360 - 180,
                        pivot = Offset(drawHeight / 2, drawWidth / 2)
                    ) {
                        with(painter) {
                            draw(
                                Size(
                                    drawWidth, drawHeight
                                ), alpha = alpha
                            )
                        }
                    }
                }
                remainingH -= pHeight
                offset = max(offset, pWidth)
            }
            remainingWidth -= offset / 1.05f
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DoodleBackgroundPreview() {
    LittleLemonTheme() {
        DoodleBackground()
    }
}

@Preview(showBackground = true, widthDp = 1024, heightDp = 768)
@Composable
private fun DoodleBackgroundPreviewTab() {
    LittleLemonTheme() {
        DoodleBackground()
    }
}