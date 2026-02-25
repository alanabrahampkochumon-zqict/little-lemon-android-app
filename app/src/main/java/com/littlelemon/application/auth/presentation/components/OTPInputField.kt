package com.littlelemon.application.auth.presentation.components

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.designsystem.xSmall

@Composable
fun OTPInputField(
    focusRequester: FocusRequester,
    number: Int?,
    onFocusChanged: (Boolean) -> Unit,
    onKeyboardBack: () -> Unit,
    onNumberChanged: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions(),
) {

    var text by remember(number) {
        mutableStateOf(
            TextFieldValue(
                text = number?.toString() ?: "",
                selection = TextRange(
                    index = if (number != null) 1 else 0
                )
            )
        )
    }

    var isFocused by remember {
        mutableStateOf(false)
    }

    val borderColor = if (errorMessage != null) {
        MaterialTheme.colors.outlineError
    } else if (isFocused) {
        MaterialTheme.colors.outlineActive
    } else if (number == null) {
        MaterialTheme.colors.secondary
    } else {
        MaterialTheme.colors.transparent
    }

    val backgroundColor = if (!enabled) {
        MaterialTheme.colors.disabled
    } else if (!isFocused) {
        MaterialTheme.colors.secondary
    } else {
        MaterialTheme.colors.primary
    }

    val textColor = if (!enabled) {
        MaterialTheme.colors.contentDisabled
    } else {
        MaterialTheme.colors.contentPrimary
    }

    Box(
        modifier = modifier
            .size(44.dp)
            .background(backgroundColor, shape = MaterialTheme.shapes.xSmall)
            .border(width = 2.dp, color = borderColor, shape = MaterialTheme.shapes.xSmall),
        contentAlignment = Alignment.Center,

        ) {
        BasicTextField(
            value = text,
            onValueChange = { newText ->
                val newNumber = newText.text.lastOrNull()
                if (newNumber == null || newNumber.isDigit()) {
                    onNumberChanged(newNumber?.digitToIntOrNull())
                }
            },
            textStyle = MaterialTheme.typeStyle.displaySmall.copy(
                textAlign = TextAlign.Center,
                color = textColor
            ),
            keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
            keyboardActions = keyboardActions,
            modifier = Modifier
                .matchParentSize()
                .focusRequester(focusRequester)
                .onFocusChanged { state ->
                    isFocused = state.isFocused
                    onFocusChanged(state.isFocused)
                }
                .onKeyEvent { event ->
                    val delPressed = event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL
                    if (delPressed && number == null) {
                        onKeyboardBack()
                    }
                    false
                },
            decorationBox = { innerBox ->
                Box(contentAlignment = Alignment.Center) {
                    innerBox()
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OTPInputFieldPreview() {
    LittleLemonTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            OTPInputField(
                number = 2,
                focusRequester = remember { FocusRequester() },
                onFocusChanged = {},
                onNumberChanged = {},
                onKeyboardBack = {},
            )
            OTPInputField(
                number = 2,
                focusRequester = remember { FocusRequester() },
                onFocusChanged = {},
                onNumberChanged = {},
                onKeyboardBack = {},
                errorMessage = "Message"
            )
            OTPInputField(
                number = 2,
                focusRequester = remember { FocusRequester() },
                onFocusChanged = {},
                onNumberChanged = {},
                onKeyboardBack = {},
                enabled = false
            )
            OTPInputField(
                number = 2,
                focusRequester = remember { FocusRequester() },
                onFocusChanged = {},
                onNumberChanged = {},
                onKeyboardBack = {},
            )
            OTPInputField(
                number = null,
                focusRequester = remember { FocusRequester() },
                onFocusChanged = {},
                onNumberChanged = {},
                onKeyboardBack = {},
            )
        }
    }
}