package com.littlelemon.application.auth.presentation

import com.littlelemon.application.core.presentation.designsystem.UiText

sealed interface AuthEvents {
    data object NavigateToOTPScreen : AuthEvents
    data object NavigateToPersonalizationScreen : AuthEvents
    data object NavigateToLocationPermissionScreen : AuthEvents
    data object NavigateBack : AuthEvents
    data object NavigateToHome : AuthEvents

    data object ShowLocationEntryPopup : AuthEvents

    data class ShowError(val errorMessage: UiText) : AuthEvents
    data class ShowInfo(val message: UiText) : AuthEvents
}