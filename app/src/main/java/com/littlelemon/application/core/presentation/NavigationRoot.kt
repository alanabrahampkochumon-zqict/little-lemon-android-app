package com.littlelemon.application.core.presentation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.littlelemon.application.auth.presentation.AuthViewModel
import com.littlelemon.application.auth.presentation.LoginScreen
import org.koin.androidx.compose.koinViewModel


@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(Route.Login)
    NavDisplay(
        backStack = backStack,
        entryProvider = { key ->
            when (key) {

                is Route.Login -> {
                    NavEntry(key) {
                        LoginScreen(koinViewModel<AuthViewModel>())
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