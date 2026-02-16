package com.littlelemon.application.core.presentation.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.ShadowSpec
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ToComposeShadowTest {

    val density = 2.0f

    @Test
    fun onConversion_nonNullShadow_createsCorrectShadow() {
        // Arrange
        val shadowSpec = ShadowSpec(
            radius = 4.0f,
            offsetX = -12.0f,
            offsetY = 8.0f,
            color = 0x000000,
            spread = 0.0f,
            alpha = 0.14f
        )

        // Act
        val shadow = shadowSpec.toComposeShadow(density)

        // Assert
        assertEquals(Color(0x000000), shadow.color)
        assertEquals((-6.0).dp, shadow.offset.x)
        assertEquals(4.dp, shadow.offset.y)
        assertEquals(0.dp, shadow.spread)
        assertEquals(2.dp, shadow.radius)
        assertEquals(0.14f, shadow.alpha)

    }
}