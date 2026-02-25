package com.littlelemon.application.auth.presentation

sealed interface AuthLoadingState {
    data object SendingOTP : AuthLoadingState
    data object ResendingOTP : AuthLoadingState
    data object VerifyingOTP : AuthLoadingState
    data object FinishingPersonalization : AuthLoadingState
}