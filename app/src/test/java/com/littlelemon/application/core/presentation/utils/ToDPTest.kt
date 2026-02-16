package com.littlelemon.application.core.presentation.utils

import androidx.compose.ui.unit.dp
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ToDPTest {

    private val screenDensity = 2.0f


    @Test
    fun onConversion_zeroPx_returnZeroDp() {
        // Arrange
        val px = 0.0f

        // Act
        val dp = px.toDP(screenDensity)

        // Assert
        assertEquals(0.dp, dp)
    }

    @Test
    fun onConversion_nonZeroPx_returnsCorrectDp() {
        // Arrange
        val px = 100.0f

        // Act
        val dp = px.toDP(screenDensity)

        // Assert
        assertEquals(50.dp, dp)
    }

}