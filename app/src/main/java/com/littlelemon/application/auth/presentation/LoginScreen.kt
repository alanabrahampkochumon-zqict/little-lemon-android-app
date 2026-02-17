package com.littlelemon.application.auth.presentation

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.littlelemon.application.R
import com.littlelemon.application.auth.presentation.components.CardLayout
import com.littlelemon.application.auth.presentation.components.DoodleBackground
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.components.Button
import com.littlelemon.application.core.presentation.designsystem.components.TextInputField
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle

@Composable
fun LoginScreen(viewModel: AuthViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val screenDensityRatio = context.resources.displayMetrics.density
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val isFloating =
        screenHeight > AuthScreenConfig.CARD_FLOATING_HEIGHT && screenWidth > AuthScreenConfig.CARD_FLOATING_WIDTH

    val maxHeight =
        if (isFloating) AuthScreenConfig.MAX_CARD_HEIGHT else Dp(AuthScreenConfig.CARD_HEIGHT_MULTIPLIER * screenHeight.value)

    val screenOrientation = configuration.orientation
    val isLandscape = screenOrientation == Configuration.ORIENTATION_LANDSCAPE

    val isScrollable = isLandscape && !isFloating

    fun onEmailChange(email: String) {
        viewModel.onAction(AuthActions.ChangeEmail(email))
    }

    fun onSendOTP() {
        Toast.makeText(context, "Sending otp...", Toast.LENGTH_LONG).show()
//        viewModel.onAction((AuthActions.SendOTP))
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars.only(
            WindowInsetsSides.Top
        ).add(WindowInsets.displayCutout).add(WindowInsets.ime)
    ) { innerPadding ->
        // Background
        DoodleBackground(modifier = Modifier)

        CardLayout(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            isFloating = isFloating,
            isScrollable = isScrollable,
            maxHeight = maxHeight,
            screenDensityRatio = screenDensityRatio
        ) {
            LoginContent(
                authState = state,
                isScrollable = isScrollable,
                onEmailChange = ::onEmailChange,
                onSendOTP = ::onSendOTP,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun ColumnScope.LoginContent(
    authState: AuthState,
    modifier: Modifier = Modifier,
    isScrollable: Boolean = false,
    onEmailChange: (String) -> Unit = {},
    onSendOTP: () -> Unit = {},
) {

    Image(
        modifier = modifier
            .align(Alignment.CenterHorizontally)
            .height(48.dp),
        painter = painterResource(R.drawable.logo_full),
        contentDescription = null
    )
    Spacer(Modifier.height(MaterialTheme.dimens.size3XL - AuthScreenConfig.FONT_OFFSET))
    Text(
        stringResource(R.string.heading_login),
        style = MaterialTheme.typeStyle.displaySmall,
        color = MaterialTheme.colors.contentPrimary,
    )
    Spacer(Modifier.height(MaterialTheme.dimens.sizeLG - AuthScreenConfig.FONT_OFFSET))

    Column(
        Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimens.sizeSM
        )
    ) {
        TextInputField(
            stringResource(R.string.placeholder_email_address),
            value = authState.email,
            errorMessage = authState.emailError,
            onValueChange = onEmailChange,
            modifier = Modifier.testTag(stringResource(R.string.test_tag_email_field))
        )
        Text(
            stringResource(R.string.body_email_description),
            style = MaterialTheme.typeStyle.bodySmall,
            color = MaterialTheme.colors.contentTertiary
        )
    }
    if (isScrollable) {
        Spacer(Modifier.height(MaterialTheme.dimens.size3XL))
    } else {
        Spacer(Modifier.weight(1f))
    }
    Button(
        stringResource(R.string.act_send_otp),
        onSendOTP,
        modifier = Modifier.fillMaxWidth(),
        enabled = authState.enableSendButton && !authState.isLoading
    )
}


@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LittleLemonTheme {
        Column {
            LoginContent(AuthState())
        }
    }
}