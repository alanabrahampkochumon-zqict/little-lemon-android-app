package com.littlelemon.application.auth.presentation

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlelemon.application.auth.domain.usecase.ValidateEmailUseCase
import com.littlelemon.application.auth.presentation.components.AuthActions
import com.littlelemon.application.core.domain.utils.DEBOUNCE_RATE_MS
import com.littlelemon.application.core.domain.utils.ValidationPatterns
import com.littlelemon.application.core.domain.utils.ValidationResult
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class AuthViewModel(
    private val validateEmailUseCase: ValidateEmailUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            snapshotFlow { _state.value.email }
                .debounce { DEBOUNCE_RATE_MS }
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
                _state.update { it.copy(firstName = action.firstName) }
            }

            is AuthActions.ChangeLastName -> {
                _state.update { it.copy(lastName = action.lastName) }
            }

            is AuthActions.ChangeOTP -> {
                _state.update { it.copy(oneTimePassword = action.otp) }
            }
        }
    }

    private fun isEmailValid(email: String): Boolean =
        ValidationPatterns.EMAIL_PATTERN.matches(email)
}