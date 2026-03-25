package com.littlelemon.application.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.CoreTestTags
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.utils.toComposeShadow

enum class LoaderColorScheme {
    DARK, LIGHT
}

@Composable
fun MiniLoader(
    label: String,
    modifier: Modifier = Modifier, colorScheme: LoaderColorScheme = LoaderColorScheme.LIGHT
) {
    val backgroundColor =
        if (colorScheme == LoaderColorScheme.DARK) MaterialTheme.colors.primaryDark
        else MaterialTheme.colors.primary


    val spinnerColor =
        if (colorScheme == LoaderColorScheme.DARK) MaterialTheme.colors.contentAccent
        else MaterialTheme.colors.contentAccentSecondary


    val contentColor =
        if (colorScheme == LoaderColorScheme.DARK) MaterialTheme.colors.contentOnColor
        else MaterialTheme.colors.contentSecondary

    val shape = MaterialTheme.shapes.medium

    val density = LocalDensity.current.density
    val firstShadow = MaterialTheme.shadows.dropMD.firstShadow.toComposeShadow(density)
    val secondShadow =
        MaterialTheme.shadows.dropMD.secondShadow?.toComposeShadow(density) ?: Shadow(0.dp)

    Row(
        modifier = modifier
            .dropShadow(shape, firstShadow)
            .dropShadow(shape, secondShadow)
            .background(backgroundColor, shape)
            .padding(
                vertical = MaterialTheme.dimens.sizeMD,
                horizontal = MaterialTheme.dimens.sizeLG
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            trackColor = MaterialTheme.colors.transparent,
            color = spinnerColor,
            strokeCap = StrokeCap.Round,
            strokeWidth = 2.dp,
            modifier = Modifier
                .size(MaterialTheme.dimens.size2XL)
                .testTag(CoreTestTags.PROGRESS_INDICATOR)
        )
        Spacer(modifier = Modifier.width(MaterialTheme.dimens.sizeLG))
        Text(label, style = MaterialTheme.typeStyle.labelMedium, color = contentColor)
    }

}

@Preview(showBackground = true)
@Composable
private fun MiniLoaderPreview() {
    LittleLemonTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MiniLoader(
                "Almost there. Tailoring your experience…",
                colorScheme = LoaderColorScheme.DARK
            )
            MiniLoader(
                "Almost there. Tailoring your experience…",
                colorScheme = LoaderColorScheme.LIGHT
            )
        }
    }
}