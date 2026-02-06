package com.littlelemon.application.auth.presentation

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlelemon.application.auth.domain.usecase.ResendOTPUseCase
import com.littlelemon.application.auth.domain.usecase.SendOTPUseCase
import com.littlelemon.application.auth.domain.usecase.ValidateEmailUseCase
import com.littlelemon.application.auth.domain.usecase.ValidateOTPUseCase
import com.littlelemon.application.auth.domain.usecase.VerifyOTPUseCase
import com.littlelemon.application.auth.domain.usecase.params.VerificationParams
import com.littlelemon.application.auth.presentation.components.AuthActions
import com.littlelemon.application.core.domain.utils.DEBOUNCE_RATE_MS
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.core.domain.utils.ValidationPatterns
import com.littlelemon.application.core.domain.utils.ValidationResult
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class AuthViewModel(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validateOTP: ValidateOTPUseCase,
    private val sendOTP: SendOTPUseCase,
    private val verifyOTP: VerifyOTPUseCase,
    private val resendOTP: ResendOTPUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    private val _navigationChannel = Channel<AuthEvents>(Channel.BUFFERED)
    val authEvent = _navigationChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            snapshotFlow { _state.value.email }.debounce { DEBOUNCE_RATE_MS }
                .collectLatest { email ->
                    if (email.isEmpty()) return@collectLatest

                    val validationResult = validateEmailUseCase(email)

                    if (validationResult is ValidationResult.Failure) {
                        _state.update {
                            it.copy(
                                emailError = validationResult.message
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(emailError = null)
                        }
                    }

                }
        }
    }


    fun onAction(action: AuthActions) {
        when (action) {
            // Field Update and Validation Actions
            is AuthActions.ChangeEmail -> {
                _state.update {
                    it.copy(
                        email = action.email,
                        emailError = null,
                        enableSendButton = isEmailValid(action.email)
                    )
                }
            }

            is AuthActions.ChangeFirstName -> {
                _state.update {
                    it.copy(
                        firstName = action.firstName,
                        enableLetsGoButton = action.firstName.isNotEmpty()
                    )
                }
            }

            is AuthActions.ChangeLastName -> {
                _state.update {
                    it.copy(
                        lastName = action.lastName,
                        enableLetsGoButton = state.value.firstName.isNotEmpty()
                    )
                }
            }

            is AuthActions.ChangeOTP -> viewModelScope.launch {
                val validationResult = validateOTP(action.otp.joinToString(""))
                when (validationResult) {
                    is ValidationResult.Failure -> _state.update {
                        it.copy(
                            oneTimePassword = action.otp,
                            otpError = null,
                            enableVerifyButton = false
                        )
                    }

                    ValidationResult.Success -> _state.update {
                        it.copy(
                            oneTimePassword = action.otp, otpError = null, enableVerifyButton = true
                        )
                    }
                }
            }

            AuthActions.NavigateBack -> viewModelScope.launch {
                _navigationChannel.send(AuthEvents.NavigateBack)
            }

            // Navigation and Related Actions
            AuthActions.ResendOTP -> viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                var event: AuthEvents? = null
                when (val result = resendOTP(state.value.email)) {
                    is Resource.Failure<*> -> {
                        event = AuthEvents.ShowError(
                            errorMessage = result.errorMessage
                                ?: "Unknown error" // TODO: Refactor to use UIText
                        )
                    }

                    Resource.Loading -> Unit // Already handled
                    is Resource.Success<*> -> {
                        event =
                            AuthEvents.ShowInfo("OTP resend successfully") // TODO: Refactor to use UIText
                    }
                }
                _state.update { it.copy(isLoading = false) }
                event?.let { authEvent ->
                    _navigationChannel.send(authEvent)
                }
            }

            AuthActions.SendOTP -> viewModelScope.launch {
                _state.update { it.copy(isLoading = true, enableSendButton = false) }
                var event: AuthEvents? = null
                when (val result = sendOTP(state.value.oneTimePassword.joinToString(""))) {
                    is Resource.Failure<*> -> {
                        event = AuthEvents.ShowError(
                            result.errorMessage ?: "An unknown error occurred!"
                        )
                    }

                    Resource.Loading -> Unit // Loading already handled
                    is Resource.Success<*> -> {
                        event = AuthEvents.NavigateToOTPScreen
                    }
                }
                _state.update { it.copy(isLoading = false, enableSendButton = true) }
                event?.let { authEvent ->
                    _navigationChannel.send(authEvent)
                }
            }

            AuthActions.VerifyOTP -> viewModelScope.launch {
                _state.update { it.copy(isLoading = true, enableVerifyButton = false) }
                var event: AuthEvents? = null
                when (val result = verifyOTP(
                    VerificationParams(
                        state.value.email, state.value.oneTimePassword.joinToString("")
                    )
                )) {
                    is Resource.Failure<*> -> {
                        event = AuthEvents.ShowError(
                            result.errorMessage ?: "An unknown error occurred!" //TODO: Refactor
                        )
                    }

                    Resource.Loading -> Unit // Loading already handled
                    is Resource.Success<*> -> {
                        event = AuthEvents.NavigateToPersonalizationScreen
                    }
                }
                _state.update { it.copy(isLoading = false, enableVerifyButton = true) }
                event?.let { authEvent ->
                    _navigationChannel.send(authEvent)
                }
            }

            AuthActions.CompletePersonalization -> viewModelScope.launch {
                _navigationChannel.send(AuthEvents.NavigateToLocationPermissionScreen)
            }

            AuthActions.ProceedToHome -> TODO()

            // Location Request and Permission
            AuthActions.RequestLocation -> TODO()

            AuthActions.RequestLocationPermission -> TODO()
        }
    }

    private fun isEmailValid(email: String): Boolean =
        ValidationPatterns.EMAIL_PATTERN.matches(email)
}