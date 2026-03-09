package com.littlelemon.application.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.designsystem.xLarge


enum class TagVariant {
    PrimaryFilled,
    SecondaryFilled,
    NeutralFilled,
    SuccessFilled,
    InformationFilled,
    ErrorFilled,
    DisabledFilled,
    PrimaryLight,
    SecondaryLight,
    NeutralLight,
    SuccessLight,
    InformationLight,
    ErrorLight,
    DisabledLight
}

/**
 * Composable function that returns the colors based on `TagVariant`.
 * @return Pair<BackgroundColor, ContentColor> for the selected variant.
 */
@Composable
private fun TagVariant.getColor(): Pair<Color, Color> {
    return when (this) {
        TagVariant.PrimaryFilled -> Pair(
            MaterialTheme.colors.action,
            MaterialTheme.colors.contentTertiary
        )

        TagVariant.SecondaryFilled -> Pair(
            MaterialTheme.colors.highlight,
            MaterialTheme.colors.contentOnColor
        )

        TagVariant.NeutralFilled -> Pair(
            MaterialTheme.colors.tertiary,
            MaterialTheme.colors.contentTertiary
        )

        TagVariant.SuccessFilled -> Pair(
            MaterialTheme.colors.success,
            MaterialTheme.colors.contentOnColor
        )

        TagVariant.InformationFilled -> Pair(
            MaterialTheme.colors.information,
            MaterialTheme.colors.contentOnColor
        )

        TagVariant.ErrorFilled -> Pair(
            MaterialTheme.colors.error,
            MaterialTheme.colors.contentOnColor
        )

        TagVariant.DisabledFilled -> Pair(
            MaterialTheme.colors.disabled,
            MaterialTheme.colors.contentPlaceholder
        )

        TagVariant.PrimaryLight -> Pair(
            MaterialTheme.colors.actionLight,
            MaterialTheme.colors.contentTertiary
        )

        TagVariant.SecondaryLight -> Pair(
            MaterialTheme.colors.highlightLight,
            MaterialTheme.colors.contentAccentSecondary
        )

        TagVariant.NeutralLight -> Pair(
            MaterialTheme.colors.primary,
            MaterialTheme.colors.contentPlaceholder
        )

        TagVariant.SuccessLight -> Pair(
            MaterialTheme.colors.successLight,
            MaterialTheme.colors.contentSuccess
        )

        TagVariant.InformationLight -> Pair(
            MaterialTheme.colors.informationLight,
            MaterialTheme.colors.contentInformation
        )

        TagVariant.ErrorLight -> Pair(
            MaterialTheme.colors.errorLight,
            MaterialTheme.colors.contentError
        )

        TagVariant.DisabledLight -> Pair(
            MaterialTheme.colors.secondary,
            MaterialTheme.colors.contentDisabled
        )
    }
}


@Composable
fun Tag(
    text: String,
    modifier: Modifier = Modifier,
    variant: TagVariant = TagVariant.PrimaryFilled
) {

    val (backgroundColor, contentColor) = variant.getColor()

    Box(
        modifier
            .background(backgroundColor, shape = MaterialTheme.shapes.xLarge)
            .padding(horizontal = MaterialTheme.dimens.sizeSM)
    ) {
        Text(text = text, style = MaterialTheme.typeStyle.bodyXSmall, color = contentColor)
    }
}


@Preview(showBackground = true)
@Composable
private fun TagPreview() {
    FlowRow(
        Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Tag("Tag", variant = TagVariant.PrimaryFilled)
        Tag("Tag", variant = TagVariant.SecondaryFilled)
        Tag("Tag", variant = TagVariant.NeutralFilled)
        Tag("Tag", variant = TagVariant.SuccessFilled)
        Tag("Tag", variant = TagVariant.InformationFilled)
        Tag("Tag", variant = TagVariant.ErrorFilled)
        Tag("Tag", variant = TagVariant.DisabledFilled)
        Tag("Tag", variant = TagVariant.PrimaryLight)
        Tag("Tag", variant = TagVariant.SecondaryLight)
        Tag("Tag", variant = TagVariant.NeutralLight)
        Tag("Tag", variant = TagVariant.SuccessLight)
        Tag("Tag", variant = TagVariant.InformationLight)
        Tag("Tag", variant = TagVariant.ErrorLight)
        Tag("Tag", variant = TagVariant.DisabledLight)
    }
}