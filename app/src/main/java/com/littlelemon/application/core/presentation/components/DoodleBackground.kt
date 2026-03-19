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
//                            drawRect(Color.Red, size = Size(drawWidth, drawHeight))
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

//
//            val gap = Random.nextInt(4.dp.toPx().roundToInt()) + 4
//            val size = Random.nextDouble(24.dp.toPx().toDouble(), 40.dp.toPx().toDouble())
//            var remainingHeight = height
//            var width = 0.0f
//            while (remainingHeight > 0) {
//                val p = painters.random()
//                val randomScale = Random.nextDouble(0.75, 1.25).toFloat()
//                val (pWidth, pHeight) = p.intrinsicSize
//                val aspectRatio = if (pWidth > pHeight) pHeight / pWidth else pWidth / pHeight
//                rotate(Random.nextDouble(-4.0, 4.0).toFloat()) {
//
//                    translate(width - remainingWidth, height - remainingHeight) {
//                        with(p) {
//                            draw(
//                                Size(
//                                    pWidth * randomScale, pHeight * randomScale
//                                )
//                            )
//                        }
//                    }
//                }
//                remainingHeight -= pHeight
//                width = max(pWidth, width)
//            }
//            remainingWidth -= width
        }
    }
}
//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .fillMaxHeight()
//            .background(MaterialTheme.colors.secondary)
//            .drawBehind {
//                val (width, height) = size
//                var remainingWidth = width
//                while (remainingWidth > 0) {
//                    val gap = Random.nextInt(4.dp.toPx().roundToInt()) + 4
//                    val size = Random.nextDouble(24.dp.toPx().toDouble(), 40.dp.toPx().toDouble())
//                    var remainingHeight = height
//                    with(rememberVectorPainter(imageBitmaps[0])) {
//                        draw(Size(size.toFloat(), size.toFloat()))
//                    }
//                    while (remainingHeight > 0) {
//                        drawRect(
//                            Color.Green,
//                            Offset(width - remainingWidth, height - remainingHeight),
//                            size = Size(size.toFloat(), size.toFloat())
//                        )
//                        remainingHeight -= size.toFloat() + gap
//                    }
//                    remainingWidth -= size.toFloat() + gap
//                }
//            }
//    ) {
//
////        Image(painterResource(drawables[0]), null)
//        Image(imageBitmaps[1], null)
//        // TODO: Implement using individual vector collection
////        Image(
////            painter = painterResource(R.drawable.doodles),
////            contentDescription = stringResource(R.string.desc_logo),
////            Modifier.fillMaxSize(),
////            contentScale = ContentScale.Crop,
////            colorFilter = ColorFilter.tint(MaterialTheme.colors.contentHighlight),
////            alpha = alpha
////        )
//    }
//}

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