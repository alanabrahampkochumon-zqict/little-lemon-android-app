package com.littlelemon.application.auth.presentation

data class AuthState(
    // Login Screen
    val email: String = "",
    val emailError: String? = null,
    val enableSendButton: Boolean = false,
    // Verify Screen
    val oneTimePassword: List<Int> = listOf(),
    val passwordError: String? = null,
    val enabledVerifyButton: Boolean = false,
    // Personalization Screen
    val firstName: String = "",
    val lastName: String = "",

    // General
    val isLoading: Boolean = false
)