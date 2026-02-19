package com.littlelemon.application.auth.presentation.screens

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
import androidx.compose.material3.minimumInteractiveComponentSize
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
import com.littlelemon.application.auth.presentation.AuthState
import com.littlelemon.application.auth.presentation.components.OTPInputField
import com.littlelemon.application.auth.presentation.components.ResendTimer
import com.littlelemon.application.core.presentation.components.Button
import com.littlelemon.application.core.presentation.components.TopNavigationBar
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationContent(
    authState: AuthState,
    modifier: Modifier = Modifier,
    isScrollable: Boolean = false,
    onOTPChange: (List<Int?>) -> Unit = {},
    onVerifyOTP: () -> Unit = {},
    onChangeEmail: () -> Unit = {},
    onOTPResend: () -> Unit = {}
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
            modifier = Modifier
                .padding(
                    start = MaterialTheme.dimens.size2XL,
                    end = MaterialTheme.dimens.size2XL,
                    bottom = MaterialTheme.dimens.sizeXL
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeXL)
        ) {
            EmailSubsection(emailAddress = authState.email, onClick = onChangeEmail)
            OTPFields(otp = authState.oneTimePassword, onOTPChange = onOTPChange)
        }
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            ResendTimer(onResendCode = onOTPResend)
        }
        if (isScrollable) {
            Spacer(Modifier.height(MaterialTheme.dimens.size3XL))
        } else {
            Spacer(Modifier.weight(1f))
        }
        Box(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size2XL)) {
            Button(
                stringResource(R.string.act_verify),
                onClick = onVerifyOTP,
                enabled = authState.enableVerifyButton
            )
        }
    }
}

@Composable
fun EmailSubsection(
    emailAddress: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clickable { onClick() }) {
        Text(
            stringResource(R.string.body_verification_code),
            style = MaterialTheme.typeStyle.bodyMedium,
            color = MaterialTheme.colors.contentSecondary
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeSM),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
private fun OTPFields(
    otp: List<Int?>,
    onOTPChange: (List<Int?>) -> Unit,
    errorMessage: String? = null
) {

    val otpSize = 6

    val focusRequesters = remember {
        List(otpSize) { FocusRequester() }
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeXS),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeMD),
        ) {
            repeat(otpSize) { index ->
                OTPInputField(
                    number = otp[index],
                    focusRequester = focusRequesters[index],
                    onFocusChanged = { },
                    onNumberChanged = { newNumber ->
                        val newOtp = otp.toMutableList()
                        newOtp[index] = newNumber
                        onOTPChange(newOtp)

                        if (index < otpSize - 1 && newNumber != null) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    },
                    onKeyboardBack = {
                        if (index > 0) {
                            focusRequesters[index - 1].requestFocus()
                        }
                    },
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
        var otp: List<Int?> = listOf(3, 1, 6, 3, 1, 6)
        VerificationContent(
            AuthState(
                email = "test@littelemon.com",
                oneTimePassword = otp
            ),
            onOTPChange = { otp = it }
        )
    }
}
