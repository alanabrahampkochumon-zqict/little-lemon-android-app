package com.littlelemon.application.auth.presentation

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
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
import com.littlelemon.application.auth.presentation.components.DoodleBackground
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.components.Button
import com.littlelemon.application.core.presentation.designsystem.components.TextInputField
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.designsystem.xLarge
import com.littlelemon.application.core.presentation.utils.toComposeShadow

@Composable
fun LoginScreen(viewModel: AuthViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp
    val screenDensityRatio = context.resources.displayMetrics.density
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
        DoodleBackground()

        LoginScreenRoot(
            authState = state,
            innerPadding = innerPadding,
            screenDensityRatio = screenDensityRatio,
            isFloating = isFloating,
            isScrollable = isScrollable,
            maxHeight = maxHeight,
            onEmailChange = ::onEmailChange,
            onSendOTP = ::onSendOTP,
            modifier = modifier
        )
    }
}

@Composable
fun LoginScreenRoot(
    authState: AuthState,
    screenDensityRatio: Float = 2f,
    maxHeight: Dp = 700.dp,
    isFloating: Boolean = false,
    isScrollable: Boolean = false,
    innerPadding: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier,
    onEmailChange: (String) -> Unit = {},
    onSendOTP: () -> Unit = {},
) {

    val scrollState = rememberScrollState()

    val cardShape = MaterialTheme.shapes.xLarge.copy(
        bottomStart = if (isFloating) MaterialTheme.shapes.large.bottomStart else CornerSize(0.dp),
        bottomEnd = if (isFloating) MaterialTheme.shapes.large.bottomEnd else CornerSize(0.dp)
    )

    Column(
        modifier
            .padding(innerPadding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (isFloating) Arrangement.Center else Arrangement.Bottom
    ) {
        Column(
            Modifier
                .widthIn(max = 480.dp)
                .dropShadow(
                    shape = cardShape,
                    shadow = MaterialTheme.shadows.upperXL.firstShadow.toComposeShadow(
                        screenDensityRatio
                    )
                )
                .dropShadow(
                    shape = cardShape,
                    shadow = MaterialTheme.shadows.upperXL.secondShadow?.toComposeShadow(
                        screenDensityRatio
                    )
                        ?: Shadow(radius = 0.dp)
                )
                .background(
                    MaterialTheme.colors.primary,
                    shape = cardShape
                )
                .padding(
                    paddingValues = PaddingValues(
                        top = MaterialTheme.dimens.size2XL,
                        end = MaterialTheme.dimens.size2XL,
                        start = MaterialTheme.dimens.size2XL,
                        bottom = if (isFloating) MaterialTheme.dimens.size2XL else MaterialTheme.dimens.size4XL,
                    )
                )
                .then(
                    if (isScrollable) Modifier
                        .fillMaxHeight()
                        .verticalScroll(scrollState)
                        .imePadding()
                    else Modifier
                        .heightIn(max = maxHeight)
                        .fillMaxHeight()
                ),
        ) {
            Image(
                modifier = Modifier
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

    }
}


@Preview
@Composable
private fun LoginScreenPreview() {
    LittleLemonTheme {
        LoginScreenRoot(AuthState())
    }
}