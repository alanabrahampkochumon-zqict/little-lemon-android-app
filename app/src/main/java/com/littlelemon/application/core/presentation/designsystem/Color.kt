package com.littlelemon.application.core.presentation.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// -------------------------------------------------------------------
// PRIMITIVES
// -------------------------------------------------------------------
@Immutable
private object PrimitivesColors {
    // DarkGreen
    val DarkGreen950 = Color(0xFF161D1B)
    val DarkGreen900 = Color(0xFF2C3532)
    val DarkGreen800 = Color(0xFF323D3A)
    val DarkGreen700 = Color(0xFF3B4A45)
    val DarkGreen600 = Color(0xFF495E57)
    val DarkGreen500 = Color(0xFF5A726A)
    val DarkGreen400 = Color(0xFF748D84)
    val DarkGreen300 = Color(0xFF9AAEA6)
    val DarkGreen200 = Color(0xFFC1CEC9)
    val DarkGreen100 = Color(0xFFE0E7E4)
    val DarkGreen50 = Color(0xFFF6F7F7)

    // Gray
    val Gray950 = Color(0xFF131416)
    val Gray900 = Color(0xFF393B40)
    val Gray800 = Color(0xFF414449)
    val Gray700 = Color(0xFF494D55)
    val Gray600 = Color(0xFF555C65)
    val Gray500 = Color(0xFF636C77)
    val Gray400 = Color(0xFF7F8891)
    val Gray300 = Color(0xFFA9B0B7)
    val Gray200 = Color(0xFFCCD1D5)
    val Gray100 = Color(0xFFE4E6E9)
    val Gray50 = Color(0xFFF5F6F6)
    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF000000)

    // Green
    val Green950 = Color(0xFF013216)
    val Green900 = Color(0xFF0E592D)
    val Green800 = Color(0xFF0E6D33)
    val Green700 = Color(0xFF0B8A3C)
    val Green600 = Color(0xFF08AA46)
    val Green500 = Color(0xFF12D55D)
    val Green400 = Color(0xFF3CEC7E)
    val Green300 = Color(0xFF7CF9AB)
    val Green200 = Color(0xFFB5FDCF)
    val Green100 = Color(0xFFD9FFE7)
    val Green50 = Color(0xFFEFFEF4)

    // Red
    val Red950 = Color(0xFF47080D)
    val Red900 = Color(0xFF831921)
    val Red800 = Color(0xFF9E1621)
    val Red700 = Color(0xFFBF1623)
    val Red600 = Color(0xFFE42433)
    val Red500 = Color(0xFFF63D48)
    val Red400 = Color(0xFFFD6C77)
    val Red300 = Color(0xFFFFA1A8)
    val Red200 = Color(0xFFFFC8CC)
    val Red100 = Color(0xFFFFE1E3)
    val Red50 = Color(0xFFFEF2F3)

    // Yellow
    val Yellow950 = Color(0xFF412207)
    val Yellow900 = Color(0xFF6F4214)
    val Yellow800 = Color(0xFF825111)
    val Yellow700 = Color(0xFF9D660B)
    val Yellow600 = Color(0xFFC58F09)
    val Yellow500 = Color(0xFFE4B80E)
    val Yellow400 = Color(0xFFF4CF14)
    val Yellow300 = Color(0xFFF8E34C)
    val Yellow200 = Color(0xFFFBF28D)
    val Yellow100 = Color(0xFFFDFAC4)
    val Yellow50 = Color(0xFFFDFDE9)

    // Blue
    val Blue950 = Color(0xFF0E315D)
    val Blue900 = Color(0xFF0D519B)
    val Blue800 = Color(0xFF085EC5)
    val Blue700 = Color(0xFF007AFD)
    val Blue600 = Color(0xFF068FFF)
    val Blue500 = Color(0xFF1EADFF)
    val Blue400 = Color(0xFF48CBFF)
    val Blue300 = Color(0xFF83DFFF)
    val Blue200 = Color(0xFFB5EAFF)
    val Blue100 = Color(0xFFD6F2FF)
    val Blue50 = Color(0xFFEDFAFF)

    // Peach
    val Peach950 = Color(0xFF3D0E0D)
    val Peach900 = Color(0xFF72211C)
    val Peach800 = Color(0xFF8D251F)
    val Peach700 = Color(0xFFB12B1D)
    val Peach600 = Color(0xFFD53C21)
    val Peach500 = Color(0xFFE3532C)
    val Peach400 = Color(0xFFE9764E)
    val Peach300 = Color(0xFFEE9972)
    val Peach200 = Color(0xFFF6C9B2)
    val Peach100 = Color(0xFFFBE5D9)
    val Peach50 = Color(0xFFFDF4EF)

    val Transparent = Color(0x00FFFFFF)
}


// -------------------------------------------------------------------
// ALIAS
// -------------------------------------------------------------------
@Immutable
private object AliasColors {
    // Primary
    val Primary950 = PrimitivesColors.Yellow950
    val Primary900 = PrimitivesColors.Yellow900
    val Primary800 = PrimitivesColors.Yellow800
    val Primary700 = PrimitivesColors.Yellow700
    val Primary600 = PrimitivesColors.Yellow600
    val Primary500 = PrimitivesColors.Yellow500
    val Primary400 = PrimitivesColors.Yellow400
    val Primary300 = PrimitivesColors.Yellow300
    val Primary200 = PrimitivesColors.Yellow200
    val Primary100 = PrimitivesColors.Yellow100
    val Primary50 = PrimitivesColors.Yellow50

    // Secondary
    val Secondary950 = PrimitivesColors.Peach950
    val Secondary900 = PrimitivesColors.Peach900
    val Secondary800 = PrimitivesColors.Peach800
    val Secondary700 = PrimitivesColors.Peach700
    val Secondary600 = PrimitivesColors.Peach600
    val Secondary500 = PrimitivesColors.Peach500
    val Secondary400 = PrimitivesColors.Peach400
    val Secondary300 = PrimitivesColors.Peach300
    val Secondary200 = PrimitivesColors.Peach200
    val Secondary100 = PrimitivesColors.Peach100
    val Secondary50 = PrimitivesColors.Peach50

    // Tertiary
    val Tertiary950 = PrimitivesColors.DarkGreen950
    val Tertiary900 = PrimitivesColors.DarkGreen900
    val Tertiary800 = PrimitivesColors.DarkGreen800
    val Tertiary700 = PrimitivesColors.DarkGreen700
    val Tertiary600 = PrimitivesColors.DarkGreen600
    val Tertiary500 = PrimitivesColors.DarkGreen500
    val Tertiary400 = PrimitivesColors.DarkGreen400
    val Tertiary300 = PrimitivesColors.DarkGreen300
    val Tertiary200 = PrimitivesColors.DarkGreen200
    val Tertiary100 = PrimitivesColors.DarkGreen100
    val Tertiary50 = PrimitivesColors.DarkGreen50

    // Success
    val Success950 = PrimitivesColors.Green950
    val Success900 = PrimitivesColors.Green900
    val Success800 = PrimitivesColors.Green800
    val Success700 = PrimitivesColors.Green700
    val Success600 = PrimitivesColors.Green600
    val Success500 = PrimitivesColors.Green500
    val Success400 = PrimitivesColors.Green400
    val Success300 = PrimitivesColors.Green300
    val Success200 = PrimitivesColors.Green200
    val Success100 = PrimitivesColors.Green100
    val Success50 = PrimitivesColors.Green50

    // Warning
    val Warning950 = PrimitivesColors.Yellow900
    val Warning900 = PrimitivesColors.Yellow900
    val Warning800 = PrimitivesColors.Yellow800
    val Warning700 = PrimitivesColors.Yellow700
    val Warning600 = PrimitivesColors.Yellow600
    val Warning500 = PrimitivesColors.Yellow500
    val Warning400 = PrimitivesColors.Yellow400
    val Warning300 = PrimitivesColors.Yellow300
    val Warning200 = PrimitivesColors.Yellow200
    val Warning100 = PrimitivesColors.Yellow100
    val Warning50 = PrimitivesColors.Yellow50

    // Error
    val Error950 = PrimitivesColors.Red950
    val Error900 = PrimitivesColors.Red900
    val Error800 = PrimitivesColors.Red800
    val Error700 = PrimitivesColors.Red700
    val Error600 = PrimitivesColors.Red600
    val Error500 = PrimitivesColors.Red500
    val Error400 = PrimitivesColors.Red400
    val Error300 = PrimitivesColors.Red300
    val Error200 = PrimitivesColors.Red200
    val Error100 = PrimitivesColors.Red100
    val Error50 = PrimitivesColors.Red50

    // Information
    val Information950 = PrimitivesColors.Blue950
    val Information900 = PrimitivesColors.Blue900
    val Information800 = PrimitivesColors.Blue800
    val Information700 = PrimitivesColors.Blue700
    val Information600 = PrimitivesColors.Blue600
    val Information500 = PrimitivesColors.Blue500
    val Information400 = PrimitivesColors.Blue400
    val Information300 = PrimitivesColors.Blue300
    val Information200 = PrimitivesColors.Blue200
    val Information100 = PrimitivesColors.Blue100
    val Information50 = PrimitivesColors.Blue50

    // Neutral
    val Neutral950 = PrimitivesColors.Gray950
    val Neutral900 = PrimitivesColors.Gray900
    val Neutral800 = PrimitivesColors.Gray800
    val Neutral700 = PrimitivesColors.Gray700
    val Neutral600 = PrimitivesColors.Gray600
    val Neutral500 = PrimitivesColors.Gray500
    val Neutral400 = PrimitivesColors.Gray400
    val Neutral300 = PrimitivesColors.Gray300
    val Neutral200 = PrimitivesColors.Gray200
    val Neutral100 = PrimitivesColors.Gray100
    val Neutral50 = PrimitivesColors.Gray50
    val NeutralBlack = PrimitivesColors.Black
    val NeutralWhite = PrimitivesColors.White

    val Transparent = PrimitivesColors.Transparent
}


@Immutable
data class LittleLemonColors(
    // Surface Colors
    val primary: Color = AliasColors.NeutralWhite,
    val primaryDark: Color = AliasColors.Tertiary700,
    val secondary: Color = AliasColors.Neutral50,
    val tertiary: Color = AliasColors.Neutral100,

    val highlight: Color = AliasColors.Secondary500,
    val highlightLight: Color = AliasColors.Secondary50,

    val action: Color = AliasColors.Primary400,
    val actionHover: Color = AliasColors.Primary300,
    val actionLight: Color = AliasColors.Primary50,
    val actionPressed: Color = AliasColors.Primary500,

    val disabled: Color = AliasColors.Neutral200,

    val warning: Color = AliasColors.Warning500,
    val warningLight: Color = AliasColors.Warning50,

    val error: Color = AliasColors.Error600,
    val errorLight: Color = AliasColors.Error50,

    val information: Color = AliasColors.Information700,
    val informationLight: Color = AliasColors.Information50,

    val success: Color = AliasColors.Success600,
    val successLight: Color = AliasColors.Success50,

    // Border/Outline
    val outlinePrimary: Color = AliasColors.Neutral200,
    val outlineSecondary: Color = AliasColors.Neutral100,

    val outlineDisabled: Color = AliasColors.Neutral50,

    val outlineHighlight: Color = AliasColors.Secondary400,
    val outlineActive: Color = AliasColors.Tertiary700,

    val outlineAccent: Color = AliasColors.Primary400,
    val outlineWarning: Color = AliasColors.Warning500,
    val outlineError: Color = AliasColors.Error600,
    val outlineSuccess: Color = AliasColors.Success600,
    val outlineInformation: Color = AliasColors.Information700,

    // Content Color -> Icons + Text
    val contentPrimary: Color = AliasColors.Neutral950,
    val contentSecondary: Color = AliasColors.Neutral900,
    val contentTertiary: Color = AliasColors.Neutral700,

    val contentPlaceholder: Color = AliasColors.Neutral500,
    val contentDisabled: Color = AliasColors.Neutral400,
    val contentInverse: Color = AliasColors.NeutralWhite,

    val contentAccent: Color = AliasColors.Primary400,
    val onAction: Color = AliasColors.Tertiary700,
    val contentAccentSecondary: Color = AliasColors.Secondary500,
    val contentHighlight: Color = AliasColors.Tertiary700,

    val contentOnColor: Color = AliasColors.NeutralWhite,

    val contentWarning: Color = AliasColors.Warning600,
    val contentError: Color = AliasColors.Error600,
    val contentSuccess: Color = AliasColors.Success600,
    val contentInformation: Color = AliasColors.Information700,

    // Transparent
    val transparent: Color = AliasColors.Transparent
)

val LocalLittleLemonColors = staticCompositionLocalOf { LittleLemonColors() }