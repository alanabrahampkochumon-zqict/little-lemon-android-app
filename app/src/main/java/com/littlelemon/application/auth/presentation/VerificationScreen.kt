package com.littlelemon.application.auth.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextAlign
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


    // FIXME: Make the list scrollable for small screen sizes like in landscape

    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation
    val isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

    val emailAddress = "mitch.lebron@gmail.com" // TODO("Replace with VM data")

    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    )
    val scaffoldModifier = if (isLandscape) modifier.nestedScroll(
        scrollBehaviour.nestedScrollConnection
    )
    else modifier

    val scrollableModifier =
        if (isLandscape) Modifier.verticalScroll(rememberScrollState()) else Modifier


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

        Column(
            modifier = scrollableModifier
                .padding(innerPadding)
                .imePadding()
                .padding(
                    start = MaterialTheme.dimens.spacingXL,
                    end = MaterialTheme.dimens.spacingXL,
                    top = MaterialTheme.dimens.spacing3XL,
                    bottom = MaterialTheme.dimens.spacingXL
                )
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            if (isLandscape) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .widthIn(max = 488.dp)
                        .fillMaxWidth()
                ) {
                    Title(emailAddress = emailAddress)
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing2XL))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            MaterialTheme.dimens.spacing2XL
                        )
                    ) {
                        OTPFields()
                        Button(
                            modifier = Modifier
                                .widthIn(max = 120.dp)
                                .height(56.dp),
                            label = stringResource(R.string.act_verify),
                            onClick = { /* TODO() */ }
                        )
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing4XL))
                    ResendSection(onResend = {/* TODO */ })
                }
            } else {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .widthIn(max = 488.dp)
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Title(emailAddress = emailAddress)
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing2XL))
                    OTPFields()
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing4XL))
                    ResendSection(onResend = {/* TODO */ })
                }
                Button(
                    modifier = Modifier
                        .widthIn(max = 488.dp)
                        .fillMaxWidth(),
                    label = stringResource(R.string.act_verify),
                    onClick = { /* TODO() */ }
                )

            }
        }
    }
}


@Composable
private fun Title(emailAddress: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.body_verification_code_send),
            style = MaterialTheme.typeStyle.bodyLarge.copy(textAlign = TextAlign.Center)
        )
        Text(
            text = emailAddress,
            style = MaterialTheme.typeStyle.labelMedium.copy(textAlign = TextAlign.Center)
        )
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
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacingMD))
        Button(
            label = stringResource(R.string.act_resend_otp),
            onClick = onResend,
            variant = ButtonVariant.GHOST
        )
        OrSeparator()
        Button(
            label = stringResource(R.string.act_change_email),
            onClick = onResend,
            variant = ButtonVariant.GHOST
        )
    }
}

@Composable
fun OrSeparator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.outlinePrimary)
                .fillMaxWidth()
                .height(1.dp)
        )
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .padding(horizontal = MaterialTheme.dimens.spacingSM)
        ) {
            Text(
                text = stringResource(R.string.or),
                style = MaterialTheme.typeStyle.labelSmall,
                color = MaterialTheme.colors.contentPlaceholder
            )
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