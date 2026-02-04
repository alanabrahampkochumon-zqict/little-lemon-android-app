package com.littlelemon.application.auth.presentation.components

sealed interface AuthActions {
    data class ChangeEmail(val email: String) : AuthActions
    data class ChangeOTP(val otp: List<Int>) : AuthActions
    data class ChangeFirstName(val firstName: String) : AuthActions
    data class ChangeLastName(val lastName: String) : AuthActions

    data object SendOTP : AuthActions

    data object VerifyOTP : AuthActions

    data object NavigateToHome : AuthActions

    data object ResendOTP : AuthActions

    data object NavigateBack : AuthActions

    //TODO Location Navigation and Triggering
}