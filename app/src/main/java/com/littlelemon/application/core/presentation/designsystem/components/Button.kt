package com.littlelemon.application.core.presentation.designsystem.components

import com.littlelemon.application.R
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle

enum class ButtonVariant {
    PRIMARY,
    SECONDARY,
    GHOST
}

@Composable
fun Button(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @DrawableRes iconLeft: Int? = null,
    @DrawableRes iconRight: Int? = null,
    variant: ButtonVariant = ButtonVariant.PRIMARY
) {
    val colors = when (variant) {
        ButtonVariant.PRIMARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colors.action,
            contentColor = MaterialTheme.colors.contentPrimary,
            disabledContainerColor = MaterialTheme.colors.actionLight,
            disabledContentColor = MaterialTheme.colors.contentDisabled,
        )

        ButtonVariant.SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.contentPrimary,
            disabledContainerColor = MaterialTheme.colors.disabled,
            disabledContentColor = MaterialTheme.colors.contentDisabled
        )

        ButtonVariant.GHOST -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colors.transparent,
            contentColor = MaterialTheme.colors.contentHighlight,
            disabledContainerColor = MaterialTheme.colors.transparent,
            disabledContentColor = MaterialTheme.colors.disabled
        )
    }

    val border: BorderStroke? = if (variant == ButtonVariant.SECONDARY) {
        BorderStroke(1.dp, MaterialTheme.colors.outlinePrimary)
    } else {
        null
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .defaultMinSize(minHeight = 48.dp)
            .then(modifier),
        colors = colors,
        shape = MaterialTheme.shapes.medium,
        border = border,
        enabled = enabled
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                MaterialTheme.dimens.spacingMD
            )
        ) {
            iconLeft?.let { icon ->
                Icon(painter = painterResource(icon), contentDescription = null)
            }
            Text(
                text = label,
                style = MaterialTheme.typeStyle.labelMedium,
            )
            iconRight?.let { icon ->
                Icon(painter = painterResource(icon), contentDescription = null)
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
                label = "Hello",
                iconRight = R.drawable.ic_checkcircle,
                iconLeft = R.drawable.ic_x
            )
            Button(
                variant = ButtonVariant.SECONDARY,
                onClick = {},
                label = "Hello",
                iconRight = R.drawable.ic_checkcircle,
                iconLeft = R.drawable.ic_x
            )
            Button(
                variant = ButtonVariant.GHOST,
                onClick = {},
                label = "Hello",
                iconRight = R.drawable.ic_checkcircle,
                iconLeft = R.drawable.ic_x
            )
            Button(
                onClick = {},
                label = "Hello",
                enabled = false,
                iconRight = R.drawable.ic_checkcircle,
                iconLeft = R.drawable.ic_x
            )
            Button(
                variant = ButtonVariant.SECONDARY,
                onClick = {},
                label = "Hello",
                enabled = false,
                iconRight = R.drawable.ic_checkcircle,
                iconLeft = R.drawable.ic_x
            )
            Button(
                variant = ButtonVariant.GHOST,
                onClick = {},
                label = "Hello",
                enabled = false,
                iconRight = R.drawable.ic_checkcircle,
                iconLeft = R.drawable.ic_x
            )
        }
    }
}