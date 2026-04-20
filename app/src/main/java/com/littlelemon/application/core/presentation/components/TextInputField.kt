package com.littlelemon.application.core.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

@Composable
fun TextInputField(
    placeholder: String,
    value: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    errorMessage: String? = null,
    @DrawableRes iconLeft: Int? = null,
    iconLeftDescription: String? = null,
    @DrawableRes iconRight: Int? = null,
    iconRightDescription: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit = {},
) {

    val interactionSource = remember { MutableInteractionSource() }

    val colors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = LittleLemonTheme.colors.contentPrimary,
        unfocusedTextColor = LittleLemonTheme.colors.contentPrimary,
        disabledTextColor = LittleLemonTheme.colors.contentDisabled,
        focusedPlaceholderColor = LittleLemonTheme.colors.contentPlaceholder,
        unfocusedPlaceholderColor = LittleLemonTheme.colors.contentPlaceholder,

        focusedContainerColor = LittleLemonTheme.colors.primary,
        unfocusedContainerColor = LittleLemonTheme.colors.secondary,
        disabledContainerColor = LittleLemonTheme.colors.disabled,

        focusedBorderColor = LittleLemonTheme.colors.outlineActive,
        unfocusedBorderColor = LittleLemonTheme.colors.transparent,
        errorBorderColor = LittleLemonTheme.colors.outlineError,
        disabledBorderColor = LittleLemonTheme.colors.transparent,

        cursorColor = LittleLemonTheme.colors.contentAccentSecondary
    )

    val iconSize = 20.dp

    Column(verticalArrangement = Arrangement.spacedBy(LittleLemonTheme.dimens.sizeXS)) {

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .fillMaxWidth()
                .then(modifier),
            enabled = enabled,
            singleLine = true,
            textStyle = LittleLemonTheme.typography.bodyMedium,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            interactionSource = interactionSource,
            visualTransformation = visualTransformation
        ) { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                placeholder = {
                    Text(text = placeholder, style = LittleLemonTheme.typography.bodyMedium)
                },
                colors = colors,
                enabled = enabled,
                leadingIcon = iconLeft?.let { icon ->
                    {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = iconLeftDescription,
                            modifier = Modifier.size(iconSize)
                        )
                    }
                },
                trailingIcon = iconRight?.let { icon ->
                    {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = iconRightDescription,
                            modifier = Modifier.size(iconSize)
                        )
                    }
                },
                singleLine = true,
                visualTransformation = visualTransformation,
                isError = errorMessage != null,
                container = {
                    OutlinedTextFieldDefaults.Container(
                        enabled,
                        errorMessage != null,
                        interactionSource,
                        focusedBorderThickness = LittleLemonTheme.dimens.size3XS,
                        colors = colors,
                        shape = LittleLemonTheme.shapes.sm,
                        modifier = Modifier.fillMaxWidth(),
                    )

                },
                contentPadding = PaddingValues(
                    horizontal = LittleLemonTheme.dimens.sizeXL,
                    vertical = LittleLemonTheme.dimens.sizeLG
                ),
                innerTextField = innerTextField,
                interactionSource = interactionSource,
                label = null,
                prefix = null,
                suffix = null,
                supportingText = null,
            )
        }
        errorMessage?.let { message ->
            Text(
                text = message,
                style = LittleLemonTheme.typography.bodyXSmall,
                color = LittleLemonTheme.colors.contentError,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun TextFieldPreview() {
    LittleLemonTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            TextInputField(
                "Test",
                "Testing...",
                iconLeft = R.drawable.ic_clipboard,
                iconRight = R.drawable.ic_x
            )
            TextInputField("Test", "")
            TextInputField("Test", "", errorMessage = "Error occurred")
            TextInputField("Test", value = "", enabled = false)
        }
    }
}