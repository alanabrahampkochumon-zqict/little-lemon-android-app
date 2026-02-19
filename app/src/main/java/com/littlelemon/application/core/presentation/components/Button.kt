package com.littlelemon.application.core.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import androidx.compose.material3.Button as M3Button

enum class ButtonVariant {
    PRIMARY,
    SECONDARY,
    GHOST,
    GHOST_HIGHLIGHT,
}

enum class ButtonSize {
    Medium,
    Large
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
    size: ButtonSize = ButtonSize.Medium
) {
    val colors = when (variant) {
        ButtonVariant.PRIMARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colors.action,
            contentColor = MaterialTheme.colors.contentOnAction,
            disabledContainerColor = MaterialTheme.colors.disabled,
            disabledContentColor = MaterialTheme.colors.contentDisabled,
        )

        // TODO: Update Variants as necessary
        ButtonVariant.SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.contentPrimary,
            disabledContainerColor = MaterialTheme.colors.transparent,
            disabledContentColor = MaterialTheme.colors.contentDisabled
        )

        ButtonVariant.GHOST -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colors.transparent,
            contentColor = MaterialTheme.colors.contentHighlight,
            disabledContainerColor = MaterialTheme.colors.transparent,
            disabledContentColor = MaterialTheme.colors.disabled
        )

        ButtonVariant.GHOST_HIGHLIGHT -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colors.transparent,
            contentColor = MaterialTheme.colors.contentAccentSecondary,
            disabledContainerColor = MaterialTheme.colors.transparent,
            disabledContentColor = MaterialTheme.colors.contentDisabled
        )
    }

    val textStyle = when (size) {
        ButtonSize.Medium -> MaterialTheme.typeStyle.labelMedium
        ButtonSize.Large -> MaterialTheme.typeStyle.labelLarge
    }

    val spacingBetween = when (size) {
        ButtonSize.Medium -> MaterialTheme.dimens.sizeMD
        ButtonSize.Large -> MaterialTheme.dimens.sizeLG
    }

    val iconSize = when (size) {
        ButtonSize.Medium -> MaterialTheme.dimens.sizeMD
        ButtonSize.Large -> MaterialTheme.dimens.sizeLG
    }

    val border: BorderStroke? = if (variant == ButtonVariant.SECONDARY) {
        BorderStroke(1.dp, MaterialTheme.colors.outlineSecondary)
    } else {
        null
    }

    M3Button(
        onClick = onClick,
        modifier = Modifier
            .defaultMinSize(minHeight = 48.dp)
            .fillMaxWidth()
            .then(modifier),
        colors = colors,
        shape = MaterialTheme.shapes.small,
        border = border,
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