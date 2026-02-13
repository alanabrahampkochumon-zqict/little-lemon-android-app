package com.littlelemon.application.core.presentation.designsystem

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.littlelemon.application.R

// Font Families
val MarkaziTextFamily = FontFamily(
    Font(R.font.markazitext_regular, FontWeight.Normal),
    Font(R.font.markazi_text_semibold, FontWeight.Bold)
)

val KarlaFontFamily = FontFamily(
    Font(R.font.karla_regular, FontWeight.Normal),
    Font(R.font.karla_medium, FontWeight.Medium),
    Font(R.font.karla_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
private val Typography = Typography(

    // Display Font
    displayLarge = TextStyle(
        fontFamily = MarkaziTextFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.21).sp
    ),
    displayMedium = TextStyle(
        fontFamily = MarkaziTextFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.21).sp
    ),
    displaySmall = TextStyle(
        fontFamily = MarkaziTextFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    // Heading Font
    headlineLarge = TextStyle(
        fontFamily = KarlaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = (-1.4).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = KarlaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.96).sp
    ),
    headlineSmall = TextStyle(
        fontFamily = KarlaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.60).sp
    ),

    labelLarge = TextStyle(
        fontFamily = KarlaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.54).sp
    ),
    labelMedium = TextStyle(
        fontFamily = KarlaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.16).sp
    ),
    labelSmall = TextStyle(
        fontFamily = KarlaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.14).sp
    ),

    // Paragraph Font -> 4 font styles, bodyLarge is the default
    bodyLarge = TextStyle(
        fontFamily = KarlaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = KarlaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontFamily = KarlaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),

    )

val Typography.bodyXSmall: TextStyle
    get() = TextStyle(
        fontFamily = KarlaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    )


// Custom Type System
@Immutable
data class LittleLemonTypography(
    val displayLarge: TextStyle = Typography.displayLarge,
    val displayMedium: TextStyle = Typography.displayMedium,
    val displaySmall: TextStyle = Typography.displaySmall,

    val headlineXLarge: TextStyle = Typography.headlineLarge,
    val headlineLarge: TextStyle = Typography.headlineMedium,
    val headlineMedium: TextStyle = Typography.headlineSmall,
    val headlineSmall: TextStyle = Typography.titleLarge,

    val labelLarge: TextStyle = Typography.labelLarge,
    val labelMedium: TextStyle = Typography.labelMedium,
    val labelSmall: TextStyle = Typography.labelSmall,

    val bodyLarge: TextStyle = Typography.titleMedium,
    val bodyMedium: TextStyle = Typography.bodyLarge,
    val bodySmall: TextStyle = Typography.bodyMedium,
    val bodyXSmall: TextStyle = Typography.bodySmall,
)

val LocalCustomTypography = staticCompositionLocalOf { LittleLemonTypography() }