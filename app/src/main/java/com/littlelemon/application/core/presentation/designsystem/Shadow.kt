package com.littlelemon.application.core.presentation.designsystem


data class ShadowLayer(
    val xOffset: Int,
    val yOffset: Int,
    val blurRadius: Int,
    val spread: Int,
    val opacity: Float,
    val color: Int = 0x000000,
)

data class Shadow(val layers: List<ShadowLayer>) {
    val dropXS: Shadow = Shadow(
        listOf(
            ShadowLayer(0, 13, 4, 0, 0.0f),
            ShadowLayer(0, 8, 3, 0, 0.01f),
            ShadowLayer(0, 5, 3, 0, 0.03f),
            ShadowLayer(0, 2, 2, 0, 0.04f),
            ShadowLayer(0, 1, 1, 0, 0.05f),
        )
    )
    val dropSM: Shadow = Shadow(
        listOf(
            ShadowLayer(0, 27, 7, 0, 0.0f),
            ShadowLayer(0, 17, 7, 0, 0.01f),
            ShadowLayer(0, 10, 6, 0, 0.03f),
            ShadowLayer(0, 4, 4, 0, 0.04f),
            ShadowLayer(0, 1, 2, 0, 0.05f),
        )
    )
    val dropMD: Shadow = Shadow(
        listOf(
            ShadowLayer(0, 40, 11, 0, 0.0f),
            ShadowLayer(0, 25, 10, 0, 0.01f),
            ShadowLayer(0, 14, 9, 0, 0.03f),
            ShadowLayer(0, 6, 6, 0, 0.04f),
            ShadowLayer(0, 2, 4, 0, 0.05f),
        )
    )
    val dropLG: Shadow = Shadow(
        listOf(
            ShadowLayer(0, 53, 15, 0, 0.0f),
            ShadowLayer(0, 34, 14, 0, 0.01f),
            ShadowLayer(0, 19, 11, 0, 0.03f),
            ShadowLayer(0, 8, 8, 0, 0.04f),
            ShadowLayer(0, 2, 5, 0, 0.05f),
        )
    )
    val dropXL: Shadow = Shadow(
        listOf(
            ShadowLayer(0, 72, 20, 0, 0.0f),
            ShadowLayer(0, 46, 18, 0, 0.01f),
            ShadowLayer(0, 26, 16, 0, 0.04f),
            ShadowLayer(0, 12, 12, 0, 0.06f),
            ShadowLayer(0, 3, 6, 0, 0.07f),
        )
    )

    val upperMD: Shadow = Shadow(
        listOf(
            ShadowLayer(0, -92, 26, 0, 0.0f),
            ShadowLayer(0, -59, 24, 0, 0.0f),
            ShadowLayer(0, -33, 20, 0, 0.02f),
            ShadowLayer(0, -15, 15, 0, 0.03f),
            ShadowLayer(0, -4, 8, 0, 0.03f),
        )
    )

    val upperLG: Shadow = Shadow(
        listOf(
            ShadowLayer(0, -188, 53, 0, 0.0f),
            ShadowLayer(0, -120, 48, 0, 0.01f),
            ShadowLayer(0, -68, 41, 0, 0.03f),
            ShadowLayer(0, -30, 30, 0, 0.04f),
            ShadowLayer(0, -8, 17, 0, 0.05f),
        )
    )

    val upperXL: Shadow = Shadow(
        listOf(
            ShadowLayer(0, -207, 58, 0, 0.0f),
            ShadowLayer(0, -133, 53, 0, 0.01f),
            ShadowLayer(0, -75, 45, 0, 0.03f),
            ShadowLayer(0, -33, 33, 0, 0.04f),
            ShadowLayer(0, -8, 18, 0, 0.05f),
        )
    )
}