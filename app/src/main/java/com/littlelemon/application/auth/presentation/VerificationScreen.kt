package com.littlelemon.application.auth.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.auth.presentation.components.OTPInputField
import com.littlelemon.application.auth.presentation.components.ResendTimer
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
    Column(modifier = modifier) {
        TopNavigationBar(
            label = stringResource(R.string.nav_verify_email),
            navigationIcon = R.drawable.ic_caretleft,
            navigationIconDescription = stringResource(R.string.act_back),
            modifier = Modifier.heightIn(max = 48.dp)
        )
        Spacer(Modifier.height(MaterialTheme.dimens.sizeXL))
        Column(
            modifier = Modifier.padding(
                start = MaterialTheme.dimens.sizeXL,
                end = MaterialTheme.dimens.sizeXL,
                bottom = MaterialTheme.dimens.sizeXL
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeXL)
        ) {
            //    EmailSubsection(emailAddress = authState.email ?: "test@gmail.com")
            EmailSubsection(emailAddress = "test@gmail.com") // TODO: Replace with VM
            OTPFields()
        }
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            ResendTimer()
        }
        if(isScrollable) {
            Spacer(Modifier.height(MaterialTheme.dimens.size3XL))
        } else {
            Spacer(Modifier.weight(1f))
        }
        Box(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.sizeXL)) {
        Button(stringResource(R.string.act_verify), onClick = onVerifyEmail)
        }
    }
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
private fun OTPFields(errorMessage: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeXS)) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeMD),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(6) {
                OTPInputField(
                    number = 2,
                    focusRequester = remember { FocusRequester() },
                    onFocusChanged = {},
                    onNumberChanged = {},
                    onKeyboardBack = {},
                )
            }
        }

        errorMessage?.let { message ->
            Text(
                text = message,
                style = MaterialTheme.typeStyle.bodySmall,
                color = MaterialTheme.colors.contentError,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VerificationScreenPreview() {
    LittleLemonTheme {
        VerificationContent(AuthState(email = "test@littelemon.com"))
    }
}
