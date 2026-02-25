package com.littlelemon.application.auth.presentation.screens

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.littlelemon.application.R
import com.littlelemon.application.auth.presentation.AuthActions
import com.littlelemon.application.auth.presentation.AuthEvents
import com.littlelemon.application.auth.presentation.AuthState
import com.littlelemon.application.auth.presentation.AuthViewModel
import com.littlelemon.application.auth.presentation.LoginRoute
import com.littlelemon.application.auth.presentation.PersonalizationRoute
import com.littlelemon.application.auth.presentation.VerificationRoute
import com.littlelemon.application.core.presentation.components.CardLayout
import com.littlelemon.application.core.presentation.components.DoodleBackground
import com.littlelemon.application.core.presentation.components.Loader
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle


enum class Step {
    Login,
    Verify,
    Personalize
}

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    isPartiallyAuthenticated: Boolean = false,
    onAuthComplete: () -> Unit = {}
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    val backStack =
        rememberNavBackStack(if (isPartiallyAuthenticated) PersonalizationRoute else LoginRoute)

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(viewModel.authEvent, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.authEvent.collect { event ->
                when (event) {
                    AuthEvents.AuthComplete -> onAuthComplete()
                    AuthEvents.NavigateBack -> backStack.removeLastOrNull()
                    AuthEvents.NavigateToOTPScreen -> backStack.add(VerificationRoute)
                    AuthEvents.NavigateToPersonalizationScreen -> {
                        backStack.removeAll(listOf(LoginRoute, VerificationRoute))
                        backStack.add(PersonalizationRoute)
                    }

                    is AuthEvents.ShowError -> {
                        Toast.makeText(context, event.errorMessage.toString(), Toast.LENGTH_LONG)
                            .show()
                        // TODO: Replace with snackbar
                    }

                    is AuthEvents.ShowInfo -> {
                        Toast.makeText(context, event.message.toString(), Toast.LENGTH_LONG).show()
                        // TODO: Replace with snackbar
                    }
                }
            }
        }

    }

    AuthScreenRoot(
        state = state,
        modifier = modifier,
        backStack = backStack,
        onUpdateEmail = { viewModel.onAction(AuthActions.ChangeEmail(it)) },
        onSendOTP = { viewModel.onAction(AuthActions.SendOTP) },
        onUpdateOTP = { viewModel.onAction(AuthActions.ChangeOTP(it)) },
        // Not updating the email. Action triggered when users taps on `change email` action.
        // Which should bring the user to the email screen.
        onChangeEmail = { viewModel.onAction(AuthActions.NavigateBack) },
        onNavigateBack = { viewModel.onAction(AuthActions.NavigateBack) },
        onVerifyOTP = { viewModel.onAction(AuthActions.VerifyOTP) },
        onUpdateFirstName = { viewModel.onAction(AuthActions.ChangeFirstName(it)) },
        onUpdateLastName = { viewModel.onAction(AuthActions.ChangeLastName(it)) },
        onCompletePersonalization = { viewModel.onAction(AuthActions.CompletePersonalization) },
        onOTPResend = { viewModel.onAction(AuthActions.ResendOTP) }
    )

}

@Composable
fun AuthScreenRoot(
    state: AuthState,
    backStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
    onUpdateEmail: (String) -> Unit = {},
    onSendOTP: () -> Unit = {},
    onUpdateOTP: (List<Int?>) -> Unit = {},
    onChangeEmail: () -> Unit = {},
    onVerifyOTP: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onUpdateFirstName: (String) -> Unit = {},
    onUpdateLastName: (String) -> Unit = {},
    onCompletePersonalization: () -> Unit = {},
    onOTPResend: () -> Unit = {}
) {
    val loaderContent: @Composable () -> Unit = {
        when (backStack.lastOrNull()) {
            is LoginRoute -> EmailVerifyLoaderContent(state.email)
            is VerificationRoute -> OTPVerificationLoaderContent()
            else -> {}
        }
    }

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

        Loader(showLoader = state.loadingState != null, loaderContent = loaderContent) {

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
                NavDisplay(
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),
                    backStack = backStack,
                    entryProvider = entryProvider {
                        entry<LoginRoute> {
                            LoginContent(
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
                        }
                        entry<VerificationRoute> {
                            VerificationContent(
                                authState = state,
                                modifier = Modifier,
                                isScrollable = isScrollable,
                                onNavigateBack = onNavigateBack,
                                onOTPChange = onUpdateOTP,
                                onChangeEmail = onChangeEmail,
                                onVerifyOTP = onVerifyOTP,
                                onOTPResend = onOTPResend,
                            )
                        }
                        entry<PersonalizationRoute> {
                            PersonalInformationContent(
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
                )
            }
        }
    }
}

@Composable
fun EmailVerifyLoaderContent(email: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            stringResource(R.string.loader_sending_verification_code),
            style = MaterialTheme.typeStyle.labelMedium,
            color = MaterialTheme.colors.contentPrimary
        )
        Text(
            email,
            style = MaterialTheme.typeStyle.labelMedium,
            color = MaterialTheme.colors.contentHighlight
        )
    }
}

@Composable
fun OTPVerificationLoaderContent() {
    Text(
        stringResource(R.string.loader_verifying_code),
        style = MaterialTheme.typeStyle.labelMedium,
        color = MaterialTheme.colors.contentPrimary
    )
}

@Preview(showBackground = true)
@Composable
private fun SendingOTPLoaderContentPreview() {
    LittleLemonTheme {
        Loader(
            showLoader = true,
            loaderContent = { EmailVerifyLoaderContent("test@gmail.com") }
        ) { }
    }
}

@Preview(showBackground = true)
@Composable
private fun VerificationLoaderContentPreview() {
    LittleLemonTheme {
        Loader(
            showLoader = true,
            loaderContent = { OTPVerificationLoaderContent() }
        ) { }
    }
}

@Preview
@Composable
private fun AuthScreenRootLoginPreview() {
    LittleLemonTheme {
        AuthScreenRoot(AuthState(), rememberNavBackStack(LoginRoute))
    }
}

@Preview
@Composable
private fun AuthScreenRootVerificationPreview() {
    LittleLemonTheme {
        AuthScreenRoot(AuthState(email = "test@gmail.com"), rememberNavBackStack(VerificationRoute))
    }
}

@Preview
@Composable
private fun AuthScreenRootPersonalizationPreview() {
    LittleLemonTheme {
        AuthScreenRoot(AuthState(), rememberNavBackStack(PersonalizationRoute))
    }
}