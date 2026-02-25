package com.littlelemon.application.auth.presentation

data class AuthState(
    // Login Screen
    val email: String = "",
    val emailError: String? = null,
    val enableSendButton: Boolean = false,

    // Verify Screen
    val oneTimePassword: List<Int?> = List(6) { null },
    val otpError: String? = null,
    val enableVerifyButton: Boolean = false,

    // Personalization Screen
    val firstName: String = "",
    val lastName: String = "",
    val enableLetsGoButton: Boolean = false,

    // General
    val loadingState: AuthLoadingState? = null,
)