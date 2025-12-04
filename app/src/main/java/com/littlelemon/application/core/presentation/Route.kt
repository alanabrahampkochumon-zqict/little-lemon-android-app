package com.littlelemon.application.core.presentation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object Login : Route, NavKey

    @Serializable
    data object Verification : Route, NavKey

    @Serializable
    data object Personalization : Route, NavKey

    @Serializable
    data object LocationPermission : Route, NavKey
}