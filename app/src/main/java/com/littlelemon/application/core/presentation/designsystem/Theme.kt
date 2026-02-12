package com.littlelemon.application.core.presentation.designsystem

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val customColors = LittleLemonColors()
val customTypography = LittleLemonTypography()
val customDimension = Dimens()
val customElevation = ShadowElevation()
val customShadow = Shadows()
private val LightColorScheme = lightColorScheme(
    primary = customColors.action,
    onPrimary = customColors.contentHighlight,

    secondary = customColors.highlight,
    onSecondary = customColors.contentInverse,

    background = customColors.primary,
    onBackground = customColors.contentPrimary,

    surface = customColors.secondary,
    onSurface = customColors.contentSecondary,

    surfaceVariant = customColors.tertiary,
    onSurfaceVariant = customColors.contentTertiary,

    outline = customColors.outlineSecondary,

    error = customColors.error,
    onError = customColors.contentInverse,
)

@Composable
fun LittleLemonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {


    //FIXME: May need to update this code
    // Forcing status bar color
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> LightColorScheme
    }

    CompositionLocalProvider(
        LocalLittleLemonColors provides customColors,
        LocalCustomTypography provides customTypography,
        LocalDimensions provides customDimension,
        LocalShadowElevation provides customElevation,
        LocalShadows provides customShadow,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
            shapes = Shapes
        )
    }
}


// Theme Extensions
val MaterialTheme.colors: LittleLemonColors
    @Composable
    get() = LocalLittleLemonColors.current

val MaterialTheme.typeStyle: LittleLemonTypography
    @Composable
    get() = LocalCustomTypography.current

val MaterialTheme.dimens: Dimens
    @Composable
    get() = LocalDimensions.current

val MaterialTheme.shadowElevation: ShadowElevation
    @Composable
    get() = LocalShadowElevation.current

val MaterialTheme.shadows: Shadows
    @Composable
    get() = LocalShadows.current