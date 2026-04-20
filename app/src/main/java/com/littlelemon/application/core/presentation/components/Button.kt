package com.littlelemon.application.core.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.utils.applyShadow
import androidx.compose.material3.Button as M3Button

enum class ButtonVariant {
    PRIMARY,
    SECONDARY,
    GHOST,
    GHOST_HIGHLIGHT,
    HIGH_CONTRAST,
}

enum class ButtonSize {
    Medium,
    Large
}

enum class ButtonShape {
    Square,
    Rounded
}

@Composable
fun Button(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @DrawableRes iconLeft: Int? = null,
    iconLeftDescription: String? = null,
    @DrawableRes iconRight: Int? = null,
    iconRightDescription: String? = null,
    variant: ButtonVariant = ButtonVariant.PRIMARY,
    size: ButtonSize = ButtonSize.Medium,
    buttonShape: ButtonShape = ButtonShape.Rounded
) {

    val colors = when (variant) {
        ButtonVariant.PRIMARY -> ButtonDefaults.buttonColors(
            containerColor = LittleLemonTheme.colors.action,
            contentColor = LittleLemonTheme.colors.contentOnAction,
            disabledContainerColor = LittleLemonTheme.colors.disabled,
            disabledContentColor = LittleLemonTheme.colors.contentDisabled,
        )

        ButtonVariant.SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = LittleLemonTheme.colors.primary,
            contentColor = LittleLemonTheme.colors.contentPrimary,
            disabledContainerColor = LittleLemonTheme.colors.transparent,
            disabledContentColor = LittleLemonTheme.colors.contentDisabled
        )

        ButtonVariant.GHOST -> ButtonDefaults.buttonColors(
            containerColor = LittleLemonTheme.colors.transparent,
            contentColor = LittleLemonTheme.colors.contentHighlight,
            disabledContainerColor = LittleLemonTheme.colors.transparent,
            disabledContentColor = LittleLemonTheme.colors.disabled
        )

        ButtonVariant.GHOST_HIGHLIGHT -> ButtonDefaults.buttonColors(
            containerColor = LittleLemonTheme.colors.transparent,
            contentColor = LittleLemonTheme.colors.contentAccentSecondary,
            disabledContainerColor = LittleLemonTheme.colors.transparent,
            disabledContentColor = LittleLemonTheme.colors.contentDisabled
        )

        ButtonVariant.HIGH_CONTRAST -> ButtonDefaults.buttonColors(
            containerColor = LittleLemonTheme.colors.highlight,
            contentColor = LittleLemonTheme.colors.contentOnColor,
            disabledContainerColor = LittleLemonTheme.colors.disabled,
            disabledContentColor = LittleLemonTheme.colors.contentDisabled
        )
    }

    val textStyle = when (size) {
        ButtonSize.Medium -> LittleLemonTheme.typography.labelMedium
        ButtonSize.Large -> LittleLemonTheme.typography.labelLarge
    }

    val spacingBetween = when (size) {
        ButtonSize.Medium -> LittleLemonTheme.dimens.sizeMD
        ButtonSize.Large -> LittleLemonTheme.dimens.sizeLG
    }

    val iconSize = when (size) {
        ButtonSize.Medium -> LittleLemonTheme.dimens.sizeMD
        ButtonSize.Large -> LittleLemonTheme.dimens.sizeLG
    }


    M3Button(
        onClick = onClick,
        modifier = Modifier
            .defaultMinSize(minHeight = 48.dp)
            .fillMaxWidth()
            .then(
                if (variant == ButtonVariant.SECONDARY && enabled) modifier.applyShadow(
                    LittleLemonTheme.shapes.sm, LittleLemonTheme.shadows.dropXS
                ) else modifier
            ),
        colors = colors,
        shape = if (buttonShape == ButtonShape.Rounded) LittleLemonTheme.shapes.sm else LittleLemonTheme.shapes.none,
        enabled = enabled
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacingBetween)
        ) {
            iconLeft?.let { icon ->
                Icon(
                    painter = painterResource(icon),
                    contentDescription = iconLeftDescription,
                    modifier = Modifier.size(iconSize)
                )
            }
            Text(
                text = label,
                style = textStyle
            )
            iconRight?.let { icon ->
                Icon(
                    painter = painterResource(icon), contentDescription = iconRightDescription,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonPreview() {
    LittleLemonTheme {
        Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {},
                label = "Button Label",
                iconRight = R.drawable.ic_checkcircle,
                iconLeft = R.drawable.ic_x
            )
            Button(
                variant = ButtonVariant.SECONDARY,
                onClick = {},
                label = "Button Label",
                iconRight = R.drawable.ic_checkcircle,
                iconLeft = R.drawable.ic_x
            )
            Button(
                variant = ButtonVariant.HIGH_CONTRAST,
                onClick = {},
                label = "Button Label",
                iconRight = R.drawable.ic_checkcircle,
                iconLeft = R.drawable.ic_x
            )
            Button(
                variant = ButtonVariant.GHOST,
                onClick = {},
                label = "Button Label",
                iconRight = R.drawable.ic_checkcircle,
                iconLeft = R.drawable.ic_x
            )
            Button(
                onClick = {},
                label = "Button Label",
                enabled = false,
                iconRight = R.drawable.ic_checkcircle,
                iconLeft = R.drawable.ic_x
            )
            Button(
                variant = ButtonVariant.SECONDARY,
                onClick = {},
                label = "Button Label",
                enabled = false,
                iconRight = R.drawable.ic_checkcircle,
                iconLeft = R.drawable.ic_x
            )
            Button(
                variant = ButtonVariant.GHOST,
                onClick = {},
                label = "Button Label",
                enabled = false,
                iconRight = R.drawable.ic_checkcircle,
                iconLeft = R.drawable.ic_x
            )
            Button(
                variant = ButtonVariant.GHOST_HIGHLIGHT,
                onClick = {},
                label = "Button Label",
                enabled = true,
                iconRight = R.drawable.ic_checkcircle,
                iconLeft = R.drawable.ic_x
            )
            Button(
                variant = ButtonVariant.GHOST_HIGHLIGHT,
                onClick = {},
                label = "Button Label",
                enabled = false,
                iconRight = R.drawable.ic_checkcircle,
                iconLeft = R.drawable.ic_x
            )
        }
    }
}