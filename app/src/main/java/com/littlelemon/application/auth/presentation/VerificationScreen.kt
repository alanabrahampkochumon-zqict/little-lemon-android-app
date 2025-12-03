package com.littlelemon.application.auth.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.auth.presentation.components.OTPInputField
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.components.Button
import com.littlelemon.application.core.presentation.designsystem.components.ButtonVariant
import com.littlelemon.application.core.presentation.designsystem.components.TopNavigationBar
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationScreen(modifier: Modifier = Modifier) {

    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation
    val isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    )
    val scaffoldModifier = if (isLandscape) modifier.nestedScroll(
        scrollBehaviour.nestedScrollConnection
    )
    else modifier
    Scaffold(
        modifier = scaffoldModifier,
        containerColor = MaterialTheme.colors.primary,
        topBar = {
            TopNavigationBar(
                navigationIcon = R.drawable.ic_caretleft,
                scrollBehaviour = if (isLandscape) scrollBehaviour else null,
                label = stringResource(R.string.verify_email)/*TODO: Implement Actions*/
            )
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            when (orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    Column(
                        modifier = Modifier
                            .padding(
                                start = MaterialTheme.dimens.spacingXL,
                                end = MaterialTheme.dimens.spacingXL,
                                top = MaterialTheme.dimens.spacing3XL,
                                bottom = MaterialTheme.dimens.spacingXL
                            )
                            .widthIn(max = 488.dp)
                            .fillMaxWidth()
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing2XL),
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = stringResource(R.string.body_verification_code_send),
                                style = MaterialTheme.typeStyle.bodyLarge
                            )
                            OTPFields()
                            ResendSection(onResend = {/* TODO */ })
                        }
                        ActionStack(onEditEmail = {/* TODO */ }, onVerify = {/* TODO */ })
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .padding(
                                horizontal = MaterialTheme.dimens.spacingXL,
                                vertical = MaterialTheme.dimens.spacingXL
                            )
                            .widthIn(max = 488.dp)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing2XL)
                    ) {
                        Text(
                            text = stringResource(R.string.body_verification_code_send),
                            style = MaterialTheme.typeStyle.bodyLarge
                        )
                        OTPFields()
                        ResendSection(onResend = {/* TODO */ })
                        ActionStack(onEditEmail = {/* TODO */ }, onVerify = {/* TODO */ })
                    }
                }
            }

        }
    }
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

@Composable
private fun ResendSection(onResend: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = stringResource(R.string.body_no_otp_received),
            style = MaterialTheme.typeStyle.labelMedium,
            color = MaterialTheme.colors.contentTertiary
        )
        Button(
            label = stringResource(R.string.act_resend_otp),
            onClick = onResend,
            variant = ButtonVariant.GHOST
        )
    }
}

@Composable
private fun ActionStack(
    modifier: Modifier = Modifier,
    onEditEmail: () -> Unit,
    onVerify: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimens.spacingLG
        )
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            label = stringResource(R.string.act_edit_email),
            onClick = onEditEmail,
            variant = ButtonVariant.SECONDARY,
            iconLeft = R.drawable.ic_pencilsimple
        )
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            label = stringResource(R.string.verify_email),
            onClick = onVerify
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun VerificationScreenPreview() {
    LittleLemonTheme {
        VerificationScreen()
    }
}