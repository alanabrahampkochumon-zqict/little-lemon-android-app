package com.littlelemon.application.auth.presentation

sealed interface AuthEvents {
    data object NavigateToOTPScreen : AuthEvents
    data object NavigateToPersonalizationScreen : AuthEvents
    data object NavigateToLocationPermissionScreen : AuthEvents
    data object NavigateBack : AuthEvents

    data class ShowError(val errorMessage: String) : AuthEvents

    data class ShowInfo(val message: String) : AuthEvents

    data object ShowLocationEntry : AuthEvents
}