package com.littlelemon.application.core.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle

@Composable
fun LabelInputField(
    label: String,
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
    Column(modifier.fillMaxWidth()) {
        Text(
            label,
            color = MaterialTheme.colors.contentSecondary,
            style = MaterialTheme.typeStyle.labelMedium
        )
        Spacer(Modifier.height(MaterialTheme.dimens.sizeSM))
        TextInputField(
            placeholder = placeholder,
            value = value,
            enabled = enabled,
            errorMessage = errorMessage,
            iconLeft = iconLeft,
            iconLeftDescription = iconLeftDescription,
            iconRight = iconRight,
            iconRightDescription = iconRightDescription,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            onValueChange = onValueChange
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LabelInputFieldPreview() {

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        LabelInputField(label = "Label", placeholder = "Placeholder", value = "")
        LabelInputField(label = "Label", placeholder = "Placeholder", value = "Value")
        LabelInputField(label = "Label", placeholder = "Placeholder", value = "", enabled = false)
        LabelInputField(
            label = "Label",
            placeholder = "Placeholder",
            value = "",
            errorMessage = "Error message"
        )
    }
}