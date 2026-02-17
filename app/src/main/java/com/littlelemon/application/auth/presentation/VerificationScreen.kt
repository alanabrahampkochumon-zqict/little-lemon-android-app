package com.littlelemon.application.auth.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.components.Button
import com.littlelemon.application.core.presentation.designsystem.components.TopNavigationBar
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationContent(
    authState: AuthState,
    modifier: Modifier = Modifier,
    isScrollable: Boolean = false,
    onVerifyEmail: () -> Unit = {},
    onChangeEmail: () -> Unit = {}
) {
    TopNavigationBar(
        label = stringResource(R.string.nav_verify_email),
        navigationIcon = R.drawable.ic_caretleft,
        navigationIconDescription = stringResource(R.string.act_back),
        modifier = Modifier.heightIn(max = 48.dp)
    )
    Spacer(Modifier.height(MaterialTheme.dimens.size3XL - AuthScreenConfig.FONT_OFFSET))
//    EmailSubsection(emailAddress = authState.email ?: "test@gmail.com")
    EmailSubsection(emailAddress = "test@gmail.com") // TODO: Replace with VM
    ResendTimer()
    Spacer(Modifier.height(MaterialTheme.dimens.size3XL - AuthScreenConfig.FONT_OFFSET))
    Button(stringResource(R.string.act_verify), onClick = onVerifyEmail)
}

@Composable
fun EmailSubsection(emailAddress: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Column(modifier = modifier.clickable { onClick() }) {
        Text(
            stringResource(R.string.body_verification_code),
            style = MaterialTheme.typeStyle.bodyMedium,
            color = MaterialTheme.colors.contentSecondary
        )
        Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeSM)) {
            Text(
                emailAddress,
                style = MaterialTheme.typeStyle.labelMedium,
                color = MaterialTheme.colors.contentOnAction
            )
            Text(
                stringResource(R.string.act_change_email),
                style = MaterialTheme.typeStyle.labelMedium,
                color = MaterialTheme.colors.contentAccentSecondary
            )
        }
    }
}

@Composable
fun ResendTimer(modifier: Modifier = Modifier) {
    Text("59")
}


@Composable
private fun OTPFields() {
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

@Preview(showBackground = true)
@Composable
private fun VerificationScreenPreview() {
    LittleLemonTheme {
            VerificationContent(AuthState(email = "test@littelemon.com"))
        }
}
