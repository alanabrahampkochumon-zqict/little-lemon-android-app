package com.littlelemon.application.core.presentation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.littlelemon.application.auth.presentation.AuthViewModel
import com.littlelemon.application.auth.presentation.screens.AuthScreen
import com.littlelemon.application.core.domain.SessionManager
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject


@Composable
fun NavigationRoot() {
    val sessionManager = koinInject<SessionManager>()
//    val sessionStatus = sessionManager.getCurrentSessionStatus()
    val backStack = rememberNavBackStack(Route.Login)
    NavDisplay(
        backStack = backStack,
        entryProvider = { key ->
            when (key) {

                is Route.Login -> {
                    NavEntry(key) {
                        AuthScreen(koinViewModel<AuthViewModel>())
                    }
                }

                is Route.Verification -> {
                    NavEntry(key) {
                        /* TODO() */
                    }
                }

                is Route.Personalization -> {
                    NavEntry(key) {
                        /*  TODO */
                    }
                }

                is Route.LocationPermission -> {
                    NavEntry(key) {
                        /* TODO() */
                    }
                }

                else -> error("Unknown NavKey: $key")
            }
        }
    )
}