package com.littlelemon.application.core.presentation.designsystem

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)
val Shapes.xLarge: RoundedCornerShape
    get() = RoundedCornerShape(40.dp)

val Shapes.None: RoundedCornerShape
    get() = RoundedCornerShape(0.dp)