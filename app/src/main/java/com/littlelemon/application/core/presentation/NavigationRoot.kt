package com.littlelemon.application.core.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.littlelemon.application.address.presentation.AddressViewModel
import com.littlelemon.application.address.presentation.screens.EnableLocationScreen
import com.littlelemon.application.auth.presentation.AuthViewModel
import com.littlelemon.application.auth.presentation.screens.AuthScreen
import com.littlelemon.application.core.domain.model.SessionStatus
import com.littlelemon.application.home.presentation.HomeScreen
import org.koin.androidx.compose.koinViewModel


@Composable
fun NavigationRoot(rootViewModel: RootViewModel = koinViewModel()) {
    var isPartiallyAuthenticated by remember {
        mutableStateOf(false)
    }
    val sessionStatus by rootViewModel.sessionStatus.collectAsStateWithLifecycle()
    val backStack = rememberNavBackStack(Route.Login)
    when (sessionStatus) {
        SessionStatus.FullyAuthenticated -> {
            isPartiallyAuthenticated = false
            backStack.remove(Route.Login)
            backStack.add(Route.Home)
        }

        SessionStatus.PartiallyAuthenticated -> {
            backStack.add(Route.Home)
            isPartiallyAuthenticated = true
        }

        SessionStatus.SessionLoading -> SplashScreen()
        SessionStatus.Unauthenticated -> Unit // Nothing needs to be done as the backstack already contains login
    }

    //TODO: Remove Log
    Log.d("Session Status", sessionStatus.toString())
    NavDisplay(
        backStack = backStack,
        entryProvider = { key ->
            when (key) {

                is Route.Login -> {
                    NavEntry(key) {
                        AuthScreen(
                            koinViewModel<AuthViewModel>(),
                            isPartiallyAuthenticated = isPartiallyAuthenticated
                        )
                    }
                }

                is Route.Home -> {
                    NavEntry(key) {
                        HomeScreen()
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