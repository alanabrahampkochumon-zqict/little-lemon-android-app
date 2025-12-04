package com.littlelemon.application.core.presentation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.littlelemon.application.auth.presentation.LoginScreen
import com.littlelemon.application.auth.presentation.PersonalInformationScreen
import com.littlelemon.application.auth.presentation.VerificationScreen


@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(Route.Login)
    NavDisplay(
        backStack = backStack,
        entryProvider = { key ->
            when (key) {

                is Route.Login -> {
                    NavEntry(key) {
                        LoginScreen({
                            backStack.add(Route.Verification)
                        })
                    }
                }

                is Route.Verification -> {
                    NavEntry(key) {
                        VerificationScreen()
                    }
                }

                is Route.Personalization -> {
                    NavEntry(key) {
                        PersonalInformationScreen()
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