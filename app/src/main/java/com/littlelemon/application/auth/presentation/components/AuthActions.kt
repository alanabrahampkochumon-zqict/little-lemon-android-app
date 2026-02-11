package com.littlelemon.application.auth.presentation.components

sealed interface AuthActions {
    data class ChangeEmail(val email: String) : AuthActions
    data class ChangeOTP(val otp: List<Int>) : AuthActions
    data class ChangeFirstName(val firstName: String) : AuthActions
    data class ChangeLastName(val lastName: String) : AuthActions
    data object SendOTP : AuthActions
    data object VerifyOTP : AuthActions
    data object ResendOTP : AuthActions
    data object CompletePersonalization : AuthActions
    data object RequestLocation : AuthActions
    data object EnterLocationManually : AuthActions
    data class SaveAddress(
        val label: String?,
        val address: String,
        val streetAddress: String,
        val city: String,
        val state: String,
        val pinCode: String,
        val latitude: Double? = null,
        val longitude: Double? = null
    ) : AuthActions // For Saving manual location

    object NavigateBack : AuthActions
}