package com.littlelemon.application.auth.presentation

import androidx.lifecycle.ViewModel
import com.littlelemon.application.auth.presentation.components.AuthActions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    fun onAction(action: AuthActions) {
        when (action) {
            is AuthActions.ChangeEmail -> {
                _state.update { it.copy(email = action.email) }
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
}