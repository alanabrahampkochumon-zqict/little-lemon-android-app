package com.littlelemon.application.core.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.utils.applyShadow
import com.littlelemon.application.core.presentation.utils.toComposeShadow

data class IconButtonColors(
    val backgroundColor: Color,
    val contentColor: Color,
    val disabledBackgroundColor: Color,
    val disabledContentColor: Color
)

@Composable
fun BasicIconButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    colors: IconButtonColors,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource? = null,
    iconDescription: String? = null,
    enabled: Boolean = true,
    shape: Shape = LittleLemonTheme.shapes.sm
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val backgroundColor = if (enabled) colors.backgroundColor else colors.disabledBackgroundColor
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .size(48.dp)
            .background(backgroundColor, shape = shape)
            .clip(shape)
            .clickable(
                enabled = enabled,
                onClick = onClick,
                interactionSource = interactionSource,
                role = Role.Button,
                indication = ripple()
            ), contentAlignment = Alignment.Center

    ) {
        Image(painterResource(icon), iconDescription, colorFilter = ColorFilter.tint(contentColor))
    }
}

@Composable
fun PrimaryIconButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconDescription: String? = null,
    showBackground: Boolean = true,
) {

    val interactionSource = remember { MutableInteractionSource() }

    val colors = IconButtonColors(
        backgroundColor = if (showBackground) LittleLemonTheme.colors.action else LittleLemonTheme.colors.transparent,
        contentColor = LittleLemonTheme.colors.contentOnAction,
        disabledBackgroundColor = if (showBackground) LittleLemonTheme.colors.disabled else LittleLemonTheme.colors.transparent,
        disabledContentColor = LittleLemonTheme.colors.contentDisabled,
    )

    BasicIconButton(icon, onClick, colors, modifier, interactionSource, iconDescription, enabled)
}

@Composable
fun SecondaryIconButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconDescription: String? = null,
    showBackground: Boolean = true,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val shape = LittleLemonTheme.shapes.sm

    val shadow = if (enabled && showBackground) {
        LittleLemonTheme.shadows.dropSM
    } else {
        null
    }

    val colors = IconButtonColors(
        backgroundColor = if (showBackground) LittleLemonTheme.colors.primary else LittleLemonTheme.colors.transparent,
        contentColor = LittleLemonTheme.colors.contentAccentSecondary,
        disabledBackgroundColor = if (showBackground) LittleLemonTheme.colors.disabled else LittleLemonTheme.colors.transparent,
        disabledContentColor = LittleLemonTheme.colors.contentDisabled,
    )

    BasicIconButton(
        icon,
        onClick,
        colors,
        modifier.then(if (shadow != null) Modifier.applyShadow(shape, shadow) else Modifier),
        interactionSource,
        iconDescription,
        enabled,
        shape
    )
}


@Composable
fun DestructiveIconButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconDescription: String? = null,
    showBackground: Boolean = true,
) {

    val interactionSource = remember { MutableInteractionSource() }

    val screenDensity = LocalDensity.current.density
    val shape = LittleLemonTheme.shapes.sm

    val shadow = if (enabled && showBackground) {
        LittleLemonTheme.shadows.dropSM
    } else {
        null
    }

    val colors = IconButtonColors(
        backgroundColor = if (showBackground) LittleLemonTheme.colors.primary else LittleLemonTheme.colors.transparent,
        contentColor = LittleLemonTheme.colors.contentError,
        disabledBackgroundColor = if (showBackground) LittleLemonTheme.colors.disabled else LittleLemonTheme.colors.transparent,
        disabledContentColor = LittleLemonTheme.colors.contentDisabled,
    )

    BasicIconButton(
        icon,
        onClick,
        colors,
        modifier.then(if (shadow != null) Modifier.applyShadow(shape, shadow) else Modifier),
        interactionSource,
        iconDescription,
        enabled,
        shape
    )
}


@Preview(showBackground = true)
@Composable
private fun PrimaryIconButtonPreview() {
    LittleLemonTheme {
        FlowRow(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            PrimaryIconButton(R.drawable.ic_plus, {})
            PrimaryIconButton(R.drawable.ic_plus, {}, enabled = false)
            PrimaryIconButton(R.drawable.ic_plus, {}, showBackground = false)
            PrimaryIconButton(R.drawable.ic_plus, {}, showBackground = false, enabled = false)

            SecondaryIconButton(R.drawable.ic_plus, {})
            SecondaryIconButton(R.drawable.ic_plus, {}, enabled = false)
            SecondaryIconButton(R.drawable.ic_plus, {}, showBackground = false)
            SecondaryIconButton(R.drawable.ic_plus, {}, showBackground = false, enabled = false)

            DestructiveIconButton(R.drawable.ic_plus, {})
            DestructiveIconButton(R.drawable.ic_plus, {}, enabled = false)
            DestructiveIconButton(R.drawable.ic_plus, {}, showBackground = false)
            DestructiveIconButton(R.drawable.ic_plus, {}, showBackground = false, enabled = false)
        }
    }
}