package com.littlelemon.application.auth.presentation.screens

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.littlelemon.application.auth.presentation.AuthActions
import com.littlelemon.application.auth.presentation.AuthViewModel
import com.littlelemon.application.core.presentation.components.CardLayout
import com.littlelemon.application.core.presentation.components.DoodleBackground
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens


enum class Step {
    Login,
    Verify,
    Personalize
}

@Composable
fun AuthScreen(viewModel: AuthViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val screenDensityRatio = context.resources.displayMetrics.density

    val (screenWidth, screenHeight) = LocalWindowInfo.current.containerDpSize

    val isFloating =
        screenHeight > AuthScreenConfig.CARD_FLOATING_HEIGHT && screenWidth > AuthScreenConfig.CARD_FLOATING_WIDTH

    val maxHeight =
        if (isFloating) AuthScreenConfig.MAX_CARD_HEIGHT else Dp(AuthScreenConfig.CARD_HEIGHT_MULTIPLIER * screenHeight.value)

    val screenOrientation = configuration.orientation
    val isLandscape = screenOrientation == Configuration.ORIENTATION_LANDSCAPE

    val isScrollable = isLandscape && !isFloating


    /**
     * Email Screen Actions
     */
    fun onUpdateEmail(email: String) {
        viewModel.onAction(AuthActions.ChangeEmail(email))
    }

    fun onSendOTP() {
        Toast.makeText(context, "Sending otp...", Toast.LENGTH_LONG).show()
//        viewModel.onAction((AuthActions.SendOTP))
    }

    /**
     * OTP Screen Actions
     */
    fun onUpdateOTP(otp: List<Int?>) {
        viewModel.onAction(AuthActions.ChangeOTP(otp))
    }

    fun onNavigateBack() {
        viewModel.onAction(AuthActions.NavigateBack)
    }

    fun onResendOTP() {
        Toast.makeText(context, "Resending otp...", Toast.LENGTH_LONG).show()
//        viewModel.onAction(AuthActions.ResendOTP)
    }

    // Not updating the email. Action triggered when users taps on `change email` action.
    // Which should bring the user to the email screen.
    fun onChangeEmail() {
        viewModel.onAction(AuthActions.NavigateBack)
    }

    fun onVerifyOTP() {
        viewModel.onAction(AuthActions.VerifyOTP)
    }

    /**
     * Personalize Screen Actions
     */
    fun onUpdateFirstName(firstName: String) {
        viewModel.onAction(AuthActions.ChangeFirstName(firstName))
    }

    fun onUpdateLastName(lastName: String) {
        viewModel.onAction(AuthActions.ChangeLastName(lastName))
    }

    fun onCompletePersonalization() {
        // TODO: Navigate
    }

    // FIXME: Remove Temporary Route
    var route by remember { mutableStateOf(Step.Verify) }
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
            maxWidth = AuthScreenConfig.MAX_CARD_WIDTH,
            screenDensityRatio = screenDensityRatio
        ) {
            when (route) {
                Step.Login -> LoginContent(
                    authState = state,
                    isScrollable = isScrollable,
                    onEmailChange = ::onUpdateEmail,
                    onSendOTP = ::onSendOTP,
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
                    onOTPChange = ::onUpdateOTP,
                    onChangeEmail = ::onChangeEmail,
                    onVerifyOTP = ::onVerifyOTP
                )

                Step.Personalize -> PersonalInformationContent(
                    state,
                    isScrollable = isScrollable,
                    onFirstNameChange = ::onUpdateFirstName,
                    onLastNameChange = ::onUpdateLastName,
                    onComplete = ::onCompletePersonalization,
                    modifier = Modifier.padding(
                        top = MaterialTheme.dimens.sizeMD,
                        start = MaterialTheme.dimens.sizeXL,
                        end = MaterialTheme.dimens.sizeXL
                    )
                )
            }

        }

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colors.action.copy(alpha = 0.5f))
        ) {
            Row {
                Text(
                    "Login",
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .clickable { route = Step.Login })
                Text(
                    "Verify",
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .clickable { route = Step.Verify })
                Text(
                    "Personalize",
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .clickable { route = Step.Personalize })
            }
        }
    }
}