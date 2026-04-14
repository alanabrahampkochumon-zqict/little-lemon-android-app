package com.littlelemon.application.auth.presentation.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.auth.presentation.AuthState
import com.littlelemon.application.core.presentation.components.Button
import com.littlelemon.application.core.presentation.components.CircularProgressBar
import com.littlelemon.application.core.presentation.components.TextInputField
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle

@Composable
fun LoginContent(
    authState: AuthState,
    modifier: Modifier = Modifier,
    isScrollable: Boolean = false,
    onEmailChange: (String) -> Unit = {},
    onSendOTP: () -> Unit = {},
) {

    Column(
        modifier = modifier
            .padding(horizontal = LittleLemonTheme.dimens.sizeMD)
    ) {
        Image(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .height(48.dp),
            painter = painterResource(R.drawable.logo_full),
            contentDescription = null
        )
        Spacer(Modifier.height(LittleLemonTheme.dimens.size2XL - AuthScreenConfig.FONT_OFFSET))
        Text(
            stringResource(R.string.heading_login),
            style = LittleLemonTheme.typography.displayLarge,
            color = LittleLemonTheme.colors.contentPrimary,
        )
        Spacer(Modifier.height(LittleLemonTheme.dimens.sizeXL - AuthScreenConfig.FONT_OFFSET))

        Column(
            Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(
                LittleLemonTheme.dimens.sizeSM
            )
        ) {
            TextInputField(
                stringResource(R.string.placeholder_email_address),
                value = authState.email,
                errorMessage = authState.emailError,
                onValueChange = onEmailChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if(authState.enableSendButton) {
                            onSendOTP()
                        }
                    }
                )
            )
            Text(
                stringResource(R.string.body_email_description),
                style = LittleLemonTheme.typography.bodySmall,
                color = LittleLemonTheme.colors.contentTertiary
            )
        }
        if (isScrollable) {
            Spacer(Modifier.height(LittleLemonTheme.dimens.size3XL))
        } else {
            Spacer(Modifier.weight(1f).animateContentSize())
        }
        CircularProgressBar(indefinite = true)
        Button(
            stringResource(R.string.act_send_otp),
            onSendOTP,
            modifier = Modifier.fillMaxWidth(),
            enabled = authState.enableSendButton && authState.loadingState == null
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun LoginContentPreview() {
    LittleLemonTheme {
        Column {
            LoginContent(AuthState())
        }
    }
}