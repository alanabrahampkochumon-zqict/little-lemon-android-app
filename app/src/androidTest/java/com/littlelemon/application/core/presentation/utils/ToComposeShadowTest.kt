package com.littlelemon.application.core.presentation.utils

import com.littlelemon.application.core.presentation.designsystem.ShadowSpec
import org.junit.Test

class ToComposeShadowTest {


    @Test
    fun onConversion_dropShadow_createsCorrectComposeShadow() {
        val dropShadow = ShadowSpec(
            radius = 3.0f,
            offsetX = 3.0f,
            offsetY = 12.0f,
            color = 0xffffff,

            )
    }
}