package com.littlelemon.application.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.littlelemon.application.address.presentation.AddressViewModel
import com.littlelemon.application.address.presentation.screens.LocationEntryContent
import com.littlelemon.application.address.presentation.screens.LocationPermissionScreen
import com.littlelemon.application.auth.presentation.AuthViewModel
import com.littlelemon.application.auth.presentation.screens.AuthScreen
import com.littlelemon.application.core.domain.model.SessionStatus
import com.littlelemon.application.core.presentation.components.BottomSheet
import com.littlelemon.application.core.presentation.components.Button
import com.littlelemon.application.home.presentation.HomeScreen
import org.koin.androidx.compose.koinViewModel


@Composable
fun NavigationRoot(rootViewModel: RootViewModel = koinViewModel()) {
    val sessionStatus by rootViewModel.sessionStatus.collectAsStateWithLifecycle()
    val userHasAddress by rootViewModel.userHasAddress.collectAsStateWithLifecycle()

    // TODO: Remove
    val context = LocalContext.current

    when (sessionStatus) {
        SessionStatus.FullyAuthenticated -> {
            if (userHasAddress == true)
                HomeScreen()
            else
                LocationPermissionScreen(koinViewModel<AddressViewModel>())
        }

        SessionStatus.PartiallyAuthenticated -> {
            AuthScreen(koinViewModel<AuthViewModel>(), isPartiallyAuthenticated = true)
        }

        SessionStatus.SessionLoading -> SplashScreen()
        SessionStatus.Unauthenticated -> {
            var show by remember { mutableStateOf(true) }
            Box(Modifier.padding(48.dp)) {
                Button("Show bottom sheet", onClick = { show = true })
            }
            val screenWidth = LocalWindowInfo.current.containerDpSize
            val isFloating = screenWidth.width > 1200.dp || screenWidth.height > 1024.dp
            // TODO: Handle better orientation
            if (isFloating)
                LocationEntryContent(koinViewModel<AddressViewModel>())
            else
                BottomSheet(show, onDismiss = { show = !show }) {
                    LocationEntryContent(koinViewModel<AddressViewModel>(), isFloating = isFloating)
                }
//            AuthScreen(koinViewModel<AuthViewModel>())
        }
    }
}