package com.littlelemon.application.menu.presentation.screen.components

import android.graphics.CornerPathEffect
import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * Defines a custom shape for `MenuCard` image.
 * @param stepperSize Size of the stepper of the menu card.
 * @param cornerRadius Corner radius of the image.
 * @param offset Any offset to apply to the shape to increase or decrease it's size.
 */
class MenuImageShape(
    private val stepperSize: Size,
    private val cornerRadius: Float,
    private val offset: Offset = Offset.Zero
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val cardPath = Path().apply {
            addRect(
                Rect(
                    0f,
                    0f,
                    size.width + offset.x,
                    size.height + offset.y
                )
            )
        }

        val stepperPath = Path().apply {
            addRect(
                Rect(
                    size.width - stepperSize.width,
                    0f,
                    size.width,
                    stepperSize.height
                )
            )
        }

        val combinedPath = Path.combine(PathOperation.Difference, cardPath, stepperPath)

        // Converts the compose path into android.graphics path to apply path effects
        // to get the rounding
        val rawAndroidPath = combinedPath.asAndroidPath()
        val paint = Paint().apply {
            pathEffect = CornerPathEffect(cornerRadius)
        }

        val roundedPath = android.graphics.Path()
        paint.getFillPath(rawAndroidPath, roundedPath)

        return Outline.Generic(roundedPath.asComposePath())
    }


}