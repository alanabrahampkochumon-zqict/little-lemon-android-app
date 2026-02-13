package com.littlelemon.application.core.presentation.designsystem.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle

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
        focusedTextColor = MaterialTheme.colors.contentPrimary,
        unfocusedTextColor = MaterialTheme.colors.contentPrimary,
        disabledTextColor = MaterialTheme.colors.contentDisabled,
        focusedPlaceholderColor = MaterialTheme.colors.contentPlaceholder,
        unfocusedPlaceholderColor = MaterialTheme.colors.contentPlaceholder,

        focusedContainerColor = MaterialTheme.colors.primary,
        unfocusedContainerColor = MaterialTheme.colors.secondary,
        disabledContainerColor = MaterialTheme.colors.disabled,

        focusedBorderColor = MaterialTheme.colors.outlineActive,
        unfocusedBorderColor = MaterialTheme.colors.transparent,
        errorBorderColor = MaterialTheme.colors.outlineError,

        cursorColor = MaterialTheme.colors.contentAccentSecondary
    )

    val iconSize = 20.dp

    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeXS)) {

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .fillMaxWidth()
                .then(modifier),
            enabled = enabled,
            singleLine = true,
            textStyle = MaterialTheme.typeStyle.labelMedium,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            interactionSource = interactionSource,
            visualTransformation = visualTransformation
        ) { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                placeholder = {
                    Text(text = placeholder, style = MaterialTheme.typeStyle.labelMedium)
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
                        focusedBorderThickness = MaterialTheme.dimens.size2XS,
                        colors = colors,
                        shape = MaterialTheme.shapes.small
                    )
                },
                contentPadding = PaddingValues(
                    horizontal = MaterialTheme.dimens.sizeXL,
                    vertical = MaterialTheme.dimens.sizeLG
                ),
                innerTextField = innerTextField,
                interactionSource = interactionSource,
                label = null,
                prefix = null,
                suffix = null,
                supportingText = null
            )
        }

//        OutlinedTextField(
//            value = value,
//            onValueChange = onValueChange,
//            placeholder = {
//                Text(text = placeholder, style = MaterialTheme.typeStyle.labelMedium)
//            },
//            modifier = Modifier
//                .minimumInteractiveComponentSize()
//                .fillMaxWidth()
//                .then(modifier),
//            colors = colors,
//            enabled = enabled,
//            leadingIcon = iconLeft?.let { icon ->
//                {
//                    Icon(
//                        painter = painterResource(icon),
//                        contentDescription = iconLeftDescription,
//                        modifier = Modifier.size(iconSize)
//                    )
//                }
//            },
//            trailingIcon = iconRight?.let { icon ->
//                {
//                    Icon(
//                        painter = painterResource(icon),
//                        contentDescription = iconRightDescription,
//                        modifier = Modifier.size(iconSize)
//                    )
//                }
//            },
//            singleLine = true,
//            keyboardOptions = keyboardOptions,
//            keyboardActions = keyboardActions,
//            visualTransformation = visualTransformation,
//            shape = MaterialTheme.shapes.small,
//            isError = errorMessage != null,
//            textStyle = MaterialTheme.typeStyle.labelMedium,
//
//            )
//        errorMessage?.let { message ->
//            Text(
//                text = message,
//                style = MaterialTheme.typeStyle.bodySmall,
//                color = MaterialTheme.colors.contentError,
//                textAlign = TextAlign.End,
//                modifier = Modifier.align(Alignment.End)
//            )
//        }
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