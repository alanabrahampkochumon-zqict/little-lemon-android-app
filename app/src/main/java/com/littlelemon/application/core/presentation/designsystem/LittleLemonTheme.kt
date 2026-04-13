package com.littlelemon.application.core.presentation.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

// TODO: Refactor to use little lemon theme
// TODO: Add shapes and dimension
@Composable
fun LittleLemonTheme(
    content: @Composable () -> Unit
) {
    val customColors = LittleLemonColors()
    val customTypography = LittleLemonTypography()
    val customShadow = LittleLemonShadows()
    val customDimensions = LittleLemonDimens()
    CompositionLocalProvider(
        LocalLittleLemonColors provides customColors,
        LocalLittleLemonTypography provides customTypography,
        LocalLittleLemonShadows provides customShadow,
        LocalLittleLemonDimensions provides customDimensions,
        content = content
    )
}


object LittleLemonTheme {
    val colors: LittleLemonColors
        @Composable
        get() = LocalLittleLemonColors.current
    val typography: LittleLemonTypography
        @Composable
        get() = LocalLittleLemonTypography.current
    val elevation: LittleLemonShadows
        @Composable
        get() = LocalLittleLemonShadows.current
    val dimens: LittleLemonDimens
        @Composable
        get() = LocalLittleLemonDimensions.current
}