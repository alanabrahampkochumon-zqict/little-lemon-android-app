package com.littlelemon.application.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme


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
            LittleLemonTheme.colors.action,
            LittleLemonTheme.colors.contentTertiary
        )

        TagVariant.SecondaryFilled -> Pair(
            LittleLemonTheme.colors.highlight,
            LittleLemonTheme.colors.contentOnColor
        )

        TagVariant.NeutralFilled -> Pair(
            LittleLemonTheme.colors.tertiary,
            LittleLemonTheme.colors.contentTertiary
        )

        TagVariant.SuccessFilled -> Pair(
            LittleLemonTheme.colors.success,
            LittleLemonTheme.colors.contentOnColor
        )

        TagVariant.InformationFilled -> Pair(
            LittleLemonTheme.colors.information,
            LittleLemonTheme.colors.contentOnColor
        )

        TagVariant.ErrorFilled -> Pair(
            LittleLemonTheme.colors.error,
            LittleLemonTheme.colors.contentOnColor
        )

        TagVariant.DisabledFilled -> Pair(
            LittleLemonTheme.colors.disabled,
            LittleLemonTheme.colors.contentPlaceholder
        )

        TagVariant.PrimaryLight -> Pair(
            LittleLemonTheme.colors.actionLight,
            LittleLemonTheme.colors.contentTertiary
        )

        TagVariant.SecondaryLight -> Pair(
            LittleLemonTheme.colors.highlightLight,
            LittleLemonTheme.colors.contentAccentSecondary
        )

        TagVariant.NeutralLight -> Pair(
            LittleLemonTheme.colors.primary,
            LittleLemonTheme.colors.contentPlaceholder
        )

        TagVariant.SuccessLight -> Pair(
            LittleLemonTheme.colors.successLight,
            LittleLemonTheme.colors.contentSuccess
        )

        TagVariant.InformationLight -> Pair(
            LittleLemonTheme.colors.informationLight,
            LittleLemonTheme.colors.contentInformation
        )

        TagVariant.ErrorLight -> Pair(
            LittleLemonTheme.colors.errorLight,
            LittleLemonTheme.colors.contentError
        )

        TagVariant.DisabledLight -> Pair(
            LittleLemonTheme.colors.secondary,
            LittleLemonTheme.colors.contentDisabled
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
            .background(backgroundColor, shape = LittleLemonTheme.shapes.xl)
            .padding(
                horizontal = LittleLemonTheme.dimens.sizeMD,
                vertical = LittleLemonTheme.dimens.size2XS
            )
    ) {
        Text(text = text, style = LittleLemonTheme.typography.bodyXSmall, color = contentColor)
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