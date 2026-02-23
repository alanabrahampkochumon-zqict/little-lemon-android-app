package com.littlelemon.application.core.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.littlelemon.application.address.presentation.AddressViewModel
import com.littlelemon.application.address.presentation.screens.EnableLocationScreen
import com.littlelemon.application.auth.presentation.AuthViewModel
import com.littlelemon.application.auth.presentation.screens.AuthScreen
import org.koin.androidx.compose.koinViewModel


@Composable
fun NavigationRoot(rootViewModel: RootViewModel = koinViewModel()) {

    val sessionStatus = rootViewModel.sessionStatus.collectAsStateWithLifecycle()
    val backStack = rememberNavBackStack(Route.LocationPermission)
//    val backStack = rememberNavBackStack(Route.LocationPermission, Route.Login)
    Log.d("Session Status", sessionStatus.value.toString())
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
                        EnableLocationScreen(koinViewModel<AddressViewModel>())
                    }
                }

                else -> error("Unknown NavKey: $key")
            }
        }
    )
}