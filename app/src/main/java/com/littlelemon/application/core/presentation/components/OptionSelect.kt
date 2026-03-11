package com.littlelemon.application.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.designsystem.xLarge
import com.littlelemon.application.core.presentation.utils.toComposeShadow

@Composable
fun OptionSelect(
    options: List<String>,
    selectedOption: String,
    onOptionChange: (option: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = MaterialTheme.shapes.xLarge

    Row(
        modifier = modifier
            .background(MaterialTheme.colors.tertiary, shape = shape)
            .padding(MaterialTheme.dimens.sizeMD)
    ) {
        options.forEach { option ->
            Option(
                option,
                selectedOption == option,
                enabled = true,
                onClick = { onOptionChange(option) },
                modifier = Modifier.innerShadow(shape, MaterialTheme.shadows.innerSM.firstShadow.toComposeShadow(LocalDensity.current.density))
            )
        }
    }
}

@Composable
fun Option(
    value: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
) {

    val backgroundColor =
        if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.tertiary
    val textColor =
        if (selected) MaterialTheme.colors.contentPrimary else if (!enabled) MaterialTheme.colors.contentDisabled else MaterialTheme.colors.contentTertiary

    val textStyle =
        if (selected) MaterialTheme.typeStyle.labelMedium else MaterialTheme.typeStyle.bodyMedium

    val shape = MaterialTheme.shapes.xLarge

    // TODO: Fix size of ripple
    Box(
        modifier = modifier
            .then(
                if (selected) Modifier.dropShadow(
                    shape,
                    MaterialTheme.shadows.dropSM.firstShadow.toComposeShadow(LocalDensity.current.density)
                ) else Modifier
            )
            .background(backgroundColor, shape)
            .padding(
                horizontal = MaterialTheme.dimens.sizeXL,
                vertical = MaterialTheme.dimens.sizeLG
            )
            .wrapContentSize()
            .clickable(
                enabled = enabled,
                onClick = onClick,
                indication = ripple(),
                interactionSource = remember { MutableInteractionSource() }),
        contentAlignment = Alignment.Center
    ) {
        Text(value, color = textColor, style = textStyle)
    }
}

@Preview
@Composable
private fun OptionSelectPreview() {
    LittleLemonTheme {
        OptionSelect(listOf("Option 1", "Option 2", "Option 3"), "Option 1", {})
    }
}