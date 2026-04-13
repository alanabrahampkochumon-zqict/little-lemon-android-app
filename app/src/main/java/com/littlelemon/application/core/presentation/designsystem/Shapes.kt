package com.littlelemon.application.core.presentation.designsystem

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)
val Shapes.xLarge: RoundedCornerShape
    get() = RoundedCornerShape(40.dp)

val Shapes.none: RoundedCornerShape
    get() = RoundedCornerShape(0.dp)

val Shapes.xSmall: RoundedCornerShape
    get() = RoundedCornerShape(8.dp)


data class LittleLemonShapes(
    val none: RoundedCornerShape = RoundedCornerShape(0.dp),
    val xs: RoundedCornerShape = RoundedCornerShape(8.dp),
    val sm: RoundedCornerShape = RoundedCornerShape(12.dp),
    val md: RoundedCornerShape = RoundedCornerShape(16.dp),
    val lg: RoundedCornerShape = RoundedCornerShape(24.dp),
    val xl: RoundedCornerShape = RoundedCornerShape(40.dp),

    val attachedCardShape: RoundedCornerShape = lg.copy(
        bottomStart = CornerSize(0.dp),
        bottomEnd = CornerSize(0.dp)
    )
)

val LocalLittleLemonShapes = compositionLocalOf { LittleLemonShapes() }