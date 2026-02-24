package com.littlelemon.application.auth.presentation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthRoute : NavKey

@Serializable
data object LoginRoute : AuthRoute

@Serializable
data object VerificationRoute : AuthRoute

@Serializable
data object PersonalizationRoute : AuthRoute