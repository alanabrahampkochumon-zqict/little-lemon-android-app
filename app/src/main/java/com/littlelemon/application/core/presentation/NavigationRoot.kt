package com.littlelemon.application.core.presentation

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.littlelemon.application.address.presentation.AddressViewModel
import com.littlelemon.application.address.presentation.screens.LocationPermissionScreen
import com.littlelemon.application.auth.presentation.AuthViewModel
import com.littlelemon.application.auth.presentation.screens.AuthScreen
import com.littlelemon.application.core.domain.model.SessionStatus
import com.littlelemon.application.home.presentation.screens.HomeScreen
import org.koin.androidx.compose.koinViewModel


@Composable
fun NavigationRoot(rootViewModel: RootViewModel = koinViewModel()) {
    val sessionStatus by rootViewModel.sessionStatus.collectAsStateWithLifecycle()
    val userHasAddress by rootViewModel.userHasAddress.collectAsStateWithLifecycle()
    // Update system bars to be dark color
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    when (sessionStatus) {
        SessionStatus.FullyAuthenticated -> {
            when (userHasAddress) {
                null -> SplashScreen()
                true -> HomeScreen()
                else -> LocationPermissionScreen(
                    koinViewModel<AddressViewModel>(),
                    onNavigate = { Log.d("Navigation", "Navigate to home") })
            }
        }

        SessionStatus.PartiallyAuthenticated -> {
            AuthScreen(koinViewModel<AuthViewModel>(), isPartiallyAuthenticated = true)
        }

        SessionStatus.SessionLoading -> SplashScreen()
        SessionStatus.Unauthenticated -> {
            AuthScreen(koinViewModel<AuthViewModel>())
        }
    }
}