package com.littlelemon.application.core.presentation.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// -------------------------------------------------------------------
// PRIMITIVES
// -------------------------------------------------------------------
// DarkGreen
private val DarkGreen900 = Color(0xFF293531)
private val DarkGreen800 = Color(0xFF3A4B45)
private val DarkGreen700 = Color(0xFF495E57)
private val DarkGreen600 = Color(0xFF59726A)
private val DarkGreen500 = Color(0xFF648278)
private val DarkGreen400 = Color(0xFF7C948C)
private val DarkGreen300 = Color(0xFF93A8A1)
private val DarkGreen200 = Color(0xFFB2C1BB)
private val DarkGreen100 = Color(0xFFD1D9D6)
private val DarkGreen50 = Color(0xFFECF0EF)

// Gray
private val Gray900 = Color(0xFF131313)
private val Gray800 = Color(0xFF333333)
private val Gray700 = Color(0xFF515151)
private val Gray600 = Color(0xFF646464)
private val Gray500 = Color(0xFF8B8B8B)
private val Gray400 = Color(0xFFACACAC)
private val Gray300 = Color(0xFFD1D1D1)
private val Gray200 = Color(0xFFE3E3E3)
private val Gray100 = Color(0xFFEEEEEE)
private val Gray50 = Color(0xFFF7F7F7)
private val White = Color(0xFFFFFFFF)
private val Black = Color(0xFF000000)

// Green
private val Green900 = Color(0xFF006600)
private val Green800 = Color(0xFF008500)
private val Green700 = Color(0xFF009613)
private val Green600 = Color(0xFF00A921)
private val Green500 = Color(0xFF04B82B)
private val Green400 = Color(0xFF47C350)
private val Green300 = Color(0xFF6ECE71)
private val Green200 = Color(0xFF9BDC9B)
private val Green100 = Color(0xFFC3E9C2)
private val Green50 = Color(0xFFE6F7E6)

// Red
private val Red900 = Color(0xFFB82614)
private val Red800 = Color(0xFFC73121)
private val Red700 = Color(0xFFD43828)
private val Red600 = Color(0xFFE6422E)
private val Red500 = Color(0xFFF54C2E)
private val Red400 = Color(0xFFF1594A)
private val Red300 = Color(0xFFE7766F)
private val Red200 = Color(0xFFF19C97)
private val Red100 = Color(0xFFFFCED0)
private val Red50 = Color(0xFFFFEBED)

// Yellow
private val Yellow900 = Color(0xFFEF7400)
private val Yellow800 = Color(0xFFF29E00)
private val Yellow700 = Color(0xFFF3B708)
private val Yellow600 = Color(0xFFF4CF14)
private val Yellow500 = Color(0xFFF3E017)
private val Yellow400 = Color(0xFFF6E545)
private val Yellow300 = Color(0xFFF8EA69)
private val Yellow200 = Color(0xFFFAF094)
private val Yellow100 = Color(0xFFFCF6BF)
private val Yellow50 = Color(0xFFFEFCE5)

private val Transparent = Color(0x00FFFFFF)

// -------------------------------------------------------------------
// ALIAS
// -------------------------------------------------------------------
// Primary
val Primary900 = Yellow900
val Primary800 = Yellow800
val Primary700 = Yellow700
val Primary600 = Yellow600
val Primary500 = Yellow500
val Primary400 = Yellow400
val Primary300 = Yellow300
val Primary200 = Yellow200
val Primary100 = Yellow100
val Primary50 = Yellow50

// Secondary
val Secondary900 = DarkGreen900
val Secondary800 = DarkGreen800
val Secondary700 = DarkGreen700
val Secondary600 = DarkGreen600
val Secondary500 = DarkGreen500
val Secondary400 = DarkGreen400
val Secondary300 = DarkGreen300
val Secondary200 = DarkGreen200
val Secondary100 = DarkGreen100
val Secondary50 =  DarkGreen50

// Success
val Success900 = Green900
val Success800 = Green800
val Success700 = Green700
val Success600 = Green600
val Success500 = Green500
val Success400 = Green400
val Success300 = Green300
val Success200 = Green200
val Success100 = Green100
val Success50 =  Green50

// Warning
val Warning900 = Yellow900
val Warning800 = Yellow800
val Warning700 = Yellow700
val Warning600 = Yellow600
val Warning500 = Yellow500
val Warning400 = Yellow400
val Warning300 = Yellow300
val Warning200 = Yellow200
val Warning100 = Yellow100
val Warning50 =  Yellow50

// Error
val Error900 = Red900
val Error800 = Red800
val Error700 = Red700
val Error600 = Red600
val Error500 = Red500
val Error400 = Red400
val Error300 = Red300
val Error200 = Red200
val Error100 = Red100
val Error50 =  Red50

// Neutral
val Neutral900 = Gray900
val Neutral800 = Gray800
val Neutral700 = Gray700
val Neutral600 = Gray600
val Neutral500 = Gray500
val Neutral400 = Gray400
val Neutral300 = Gray300
val Neutral200 = Gray200
val Neutral100 = Gray100
val Neutral50 =  Gray50
val NeutralBlack =  Black
val NeutralWhite =  White


@Immutable
data class LittleLemonColors(
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
    val contentSuccess: Color = Success800,

    // Transparent
    val transparent: Color = Transparent
)

val LocalLittleLemonColors = staticCompositionLocalOf { LittleLemonColors() }