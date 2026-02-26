package com.littlelemon.application.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.littlelemon.application.address.presentation.AddressViewModel
import com.littlelemon.application.address.presentation.screens.EnableLocationScreen
import com.littlelemon.application.auth.presentation.AuthViewModel
import com.littlelemon.application.auth.presentation.screens.AuthScreen
import com.littlelemon.application.core.domain.model.SessionStatus
import com.littlelemon.application.home.presentation.HomeScreen
import org.koin.androidx.compose.koinViewModel


@Composable
fun NavigationRoot(rootViewModel: RootViewModel = koinViewModel()) {
    val sessionStatus by rootViewModel.sessionStatus.collectAsStateWithLifecycle()
    val userHasAddress by rootViewModel.userHasAddress.collectAsStateWithLifecycle()
    when (sessionStatus) {
        SessionStatus.FullyAuthenticated -> {
            if (userHasAddress == true)
                HomeScreen()
            else
                EnableLocationScreen(koinViewModel<AddressViewModel>())
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