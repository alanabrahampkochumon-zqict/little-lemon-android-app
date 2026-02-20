package com.littlelemon.application.auth.presentation.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.littlelemon.application.auth.presentation.AuthActions
import com.littlelemon.application.auth.presentation.AuthState
import com.littlelemon.application.auth.presentation.AuthViewModel
import com.littlelemon.application.core.presentation.components.CardLayout
import com.littlelemon.application.core.presentation.components.DoodleBackground
import com.littlelemon.application.core.presentation.components.Loader
import com.littlelemon.application.core.presentation.designsystem.dimens


enum class Step {
    Login,
    Verify,
    Personalize
}

@Composable
fun AuthScreen(viewModel: AuthViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AuthScreenRoot(
        state = state,
        modifier = modifier,
        onUpdateEmail = { viewModel.onAction(AuthActions.ChangeEmail(it)) },
        onSendOTP = { viewModel.onAction(AuthActions.SendOTP) },
        onUpdateOTP = { viewModel.onAction(AuthActions.ChangeOTP(it)) },
        // Not updating the email. Action triggered when users taps on `change email` action.
        // Which should bring the user to the email screen.
        onChangeEmail = { viewModel.onAction(AuthActions.NavigateBack) },
        onVerifyOTP = { viewModel.onAction(AuthActions.VerifyOTP) },
        onUpdateFirstName = { viewModel.onAction(AuthActions.ChangeFirstName(it)) },
        onUpdateLastName = { viewModel.onAction(AuthActions.ChangeLastName(it)) },
        onCompletePersonalization = { viewModel.onAction(AuthActions.CompletePersonalization) }
    )


}

@Composable
fun AuthScreenRoot(
    state: AuthState,
    modifier: Modifier = Modifier,
    route: Step? = null, // TODO: Change
    onUpdateEmail: (String) -> Unit = {},
    onSendOTP: () -> Unit = {},
    onUpdateOTP: (List<Int?>) -> Unit = {},
    onChangeEmail: () -> Unit = {},
    onVerifyOTP: () -> Unit = {},
    onUpdateFirstName: (String) -> Unit = {},
    onUpdateLastName: (String) -> Unit = {},
    onCompletePersonalization: () -> Unit = {}
) {

    // FIXME: Remove Temporary Route
    var route by remember { mutableStateOf(Step.Verify) }

    val screenDensityRatio = LocalDensity.current.density
    val (screenWidth, screenHeight) = LocalWindowInfo.current.containerDpSize

    val isFloating =
        screenHeight > AuthScreenConfig.CARD_FLOATING_HEIGHT && screenWidth > AuthScreenConfig.CARD_FLOATING_WIDTH

    val maxHeight =
        if (isFloating) AuthScreenConfig.MAX_CARD_HEIGHT else Dp(AuthScreenConfig.CARD_HEIGHT_MULTIPLIER * screenHeight.value)

    val configuration = LocalConfiguration.current
    val screenOrientation = configuration.orientation
    val isLandscape = screenOrientation == Configuration.ORIENTATION_LANDSCAPE

    val isScrollable = isLandscape && !isFloating

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars.only(
            WindowInsetsSides.Top
        ).add(WindowInsets.displayCutout).add(WindowInsets.ime)
    ) { innerPadding ->

        Loader(showLoader = state.isLoading) {

            DoodleBackground()

            //Content
            CardLayout(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                isFloating = isFloating,
                isScrollable = isScrollable,
                maxHeight = maxHeight,
                maxWidth = AuthScreenConfig.MAX_CARD_WIDTH,
                screenDensityRatio = screenDensityRatio
            ) {
                when (route) {
                    Step.Login -> LoginContent(
                        authState = state,
                        isScrollable = isScrollable,
                        onEmailChange = onUpdateEmail,
                        onSendOTP = onSendOTP,
                        modifier = Modifier.padding(
                            top = MaterialTheme.dimens.sizeMD,
                            start = MaterialTheme.dimens.sizeXL,
                            end = MaterialTheme.dimens.sizeXL
                        )
                    )

                    Step.Verify -> VerificationContent(
                        authState = state,
                        modifier = Modifier,
                        isScrollable = isScrollable,
                        onOTPChange = onUpdateOTP,
                        onChangeEmail = onChangeEmail,
                        onVerifyOTP = onVerifyOTP
                    )

                    Step.Personalize -> PersonalInformationContent(
                        state,
                        isScrollable = isScrollable,
                        onFirstNameChange = onUpdateFirstName,
                        onLastNameChange = onUpdateLastName,
                        onComplete = onCompletePersonalization,
                        modifier = Modifier.padding(
                            top = MaterialTheme.dimens.sizeMD,
                            start = MaterialTheme.dimens.sizeXL,
                            end = MaterialTheme.dimens.sizeXL
                        )
                    )
                }
            }
        }
    }
}