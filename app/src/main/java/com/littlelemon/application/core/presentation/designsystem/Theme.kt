package com.littlelemon.application.core.presentation.designsystem

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

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

@Immutable
data class CustomColors(
    // Surface Colors
    val primary: Color = NeutralWhite,
    val secondary: Color = Neutral50,
    val tertiary: Color = Neutral100,
    val highlight: Color = Secondary600,
    val highlightLight: Color = Secondary100,
    val action: Color = Primary600,
    val actionLight: Color = Primary100,
    val actionHover: Color = Primary500,
    val actionPressed: Color = Primary700,
    val disabled: Color = Neutral100,
    val warning: Color = Warning800,
    val warningLight: Color = Warning50,
    val error: Color = Error800,
    val errorLight: Color = Error50,
    val success: Color = Success800,
    val successLight: Color = Success50,

    // Border/Outline
    val outlinePrimary: Color = Neutral300,
    val outlineSecondary: Color = Neutral200,
    val outlineDisabled: Color = Neutral200,
    val outlineHighlight: Color = Secondary700,
    val outlineActive: Color = Neutral800,
    val outlineAccent: Color = Primary600,
    val outlineWarning: Color = Warning700,
    val outlineError: Color = Error700,
    val outlineSuccess: Color = Success700,

    // Content Color -> Icons + Text
    val contentPrimary: Color = Neutral900,
    val contentSecondary: Color = Neutral800,
    val contentTertiary: Color = Neutral700,
    val contentPlaceholder: Color = Neutral600,
    val contentDisabled: Color = Neutral400,
    val contentAccent: Color = Primary700,
    val contentHighlight: Color = Secondary700,
    val contentOnColor: Color = NeutralWhite,
    val contentWarning: Color = Warning700,
    val contentError: Color = Error700,
    val contentSuccess: Color = Success800
    )

val LocalCustomColors = staticCompositionLocalOf { CustomColors() }

@Composable
fun LittleLemonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val customColors = CustomColors()

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    CompositionLocalProvider(LocalCustomColors provides customColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
            shapes = Shapes
        )
    }
}

val MaterialTheme.customColors: CustomColors
    @Composable
    get() = LocalCustomColors.current