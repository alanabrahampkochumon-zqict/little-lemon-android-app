package com.littlelemon.application.core.presentation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object Login : Route

    @Serializable
    data object _Personalize : Route {
    }

    @Serializable
    data object LocationPermission : Route


    @Serializable
    data object Home : Route
}