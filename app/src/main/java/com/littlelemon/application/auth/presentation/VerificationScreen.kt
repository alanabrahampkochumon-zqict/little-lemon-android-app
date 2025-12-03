package com.littlelemon.application.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.auth.presentation.components.OTPInputField
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.components.TopNavigationBar

@Composable
fun VerificationScreen(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier, topBar = {
        TopNavigationBar(
            navigationIcon = R.drawable.ic_caretleft,
            label = stringResource(R.string.verify_email)/*TODO: Implement Actions*/
        )
    }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(text = stringResource(R.string.body_verification_code_send))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
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
                )
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
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VerificationScreenPreview() {
    LittleLemonTheme {
        VerificationScreen()
    }
}