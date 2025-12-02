package com.littlelemon.application.core.presentation.designsystem

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Primary700,
    onPrimary = Neutral900,

    secondary = Secondary700,
    onSecondary = NeutralWhite,

    background = NeutralWhite,
    onBackground = Neutral900,

    surface = NeutralWhite,
    onSurface = Neutral900,

    surfaceVariant = NeutralWhite,
    onSurfaceVariant = Neutral800,

    outline = Neutral300,

    error = Error700,
    onError = NeutralWhite,
)

@Composable
fun LittleLemonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val customColors = LittleLemonColors()
    val customTypography = LittleLemonTypography()
    val customDimension = Dimens()


    // Forcing status bar color
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // --- Force Status Bar Color Here ---
            // Example 1: Force it to your M3 Primary color:
            // window.statusBarColor = colorScheme.primary.toArgb()

            // Example 2: Force it to a specific, static color:
            // window.statusBarColor = Color.Magenta.toArgb()

            // The following line makes the icons on the status bar dark/light
            // based on the luminance of the color you set above.
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(
        LocalLittleLemonColors provides customColors,
        LocalCustomTypography provides customTypography,
        LocalDimensions provides customDimension
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