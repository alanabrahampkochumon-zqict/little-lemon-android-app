package com.littlelemon.application.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.utils.applyShadow

@Composable
fun OptionSelect(
    options: List<String>,
    selectedOption: String,
    onSelectionChange: (option: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = LittleLemonTheme.shapes.xl
    Row(
        modifier = modifier
            .background(LittleLemonTheme.colors.tertiary, shape = shape)
            .padding(LittleLemonTheme.dimens.sizeMD),
    ) {
        options.forEach { option ->
            Option(
                value = option,
                selected = selectedOption == option,
                enabled = true,
                onClick = { onSelectionChange(option) },
                modifier = Modifier

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
        if (selected) LittleLemonTheme.colors.primary else LittleLemonTheme.colors.tertiary
    val textColor =
        if (selected) LittleLemonTheme.colors.contentPrimary else if (!enabled) LittleLemonTheme.colors.contentDisabled else LittleLemonTheme.colors.contentTertiary

    val textStyle =
        if (selected) LittleLemonTheme.typography.labelMedium else LittleLemonTheme.typography.bodyMedium

    val shape = LittleLemonTheme.shapes.xl

    // TODO: Add animation
    Box(
        modifier = modifier
            .then(
                if (selected) Modifier.applyShadow(
                    shape,
                    LittleLemonTheme.shadows.dropXS
                ) else Modifier
            )
            .background(backgroundColor, shape)
            .selectable(
                selected = selected,
                enabled = enabled,
                onClick = onClick,
                indication = null,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() })
            .padding(
                horizontal = LittleLemonTheme.dimens.sizeXL,
                vertical = LittleLemonTheme.dimens.sizeLG
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(value, color = textColor, style = textStyle)
    }
}

@Preview(showBackground = true)
@Composable
private fun OptionSelectPreview() {
    val options = listOf("Option 1", "Option 2", "Option 3")
    val currentOption = remember { mutableStateOf(options[0]) }
    LittleLemonTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            OptionSelect(options, currentOption.value, { currentOption.value = it })
        }
    }
}