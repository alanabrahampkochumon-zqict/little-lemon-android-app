package com.littlelemon.application.core.presentation

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.littlelemon.application.address.presentation.AddressViewModel
import com.littlelemon.application.address.presentation.screens.LocationPermissionScreen
import com.littlelemon.application.auth.presentation.AuthViewModel
import com.littlelemon.application.auth.presentation.screens.AuthScreen
import com.littlelemon.application.core.domain.model.SessionStatus
import com.littlelemon.application.home.presentation.HomeScreen
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.models.NutritionInfo
import com.littlelemon.application.menu.presentation.screen.components.MenuCard
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Clock


@Composable
fun NavigationRoot(rootViewModel: RootViewModel = koinViewModel()) {
    val sessionStatus by rootViewModel.sessionStatus.collectAsStateWithLifecycle()
    val userHasAddress by rootViewModel.userHasAddress.collectAsStateWithLifecycle()

    when (sessionStatus) {
        SessionStatus.FullyAuthenticated -> {
            if (userHasAddress == true)
                HomeScreen()
            else
                FlowRow {
                    MenuCard(
                        Dish(
                            title = "Grilled Whole Fish",
                            description = "The warm bread is rubbed with raw garlic cloves known for immune-boosting  and anti-inflammatory properties and generously drizzled with  extra-virgin olive oil (EVOO), the primary source of monounsaturated  fats in this diet",
                            price = 29.85,
                            imageURL = "NO URL",
                            stock = 15,
                            nutritionInfo = NutritionInfo(155, 22, 15, 9),
                            discountedPrice = 15.83,
                            category = listOf(),
                            dateAdded = Clock.System.now()
                                .toLocalDateTime(TimeZone.currentSystemDefault()),
                            popularityIndex = 392
                        )
                    )
                    MenuCard(
                        Dish(
                            title = "Grilled Whole Fish",
                            description = "The warm bread is rubbed with raw garlic cloves known for immune-boosting  and anti-inflammatory properties and generously drizzled with  extra-virgin olive oil (EVOO), the primary source of monounsaturated  fats in this diet",
                            price = 29.85,
                            imageURL = "NO URL",
                            stock = 15,
                            nutritionInfo = NutritionInfo(155, 22, 15, 9),
                            discountedPrice = 15.83,
                            category = listOf(),
                            dateAdded = Clock.System.now()
                                .toLocalDateTime(TimeZone.currentSystemDefault()),
                            popularityIndex = 392
                        )
                    )
                    MenuCard(
                        Dish(
                            title = "Grilled Whole Fish",
                            description = "The warm bread is rubbed with raw garlic cloves known for immune-boosting  and anti-inflammatory properties and generously drizzled with  extra-virgin olive oil (EVOO), the primary source of monounsaturated  fats in this diet",
                            price = 29.85,
                            imageURL = "NO URL",
                            stock = 15,
                            nutritionInfo = NutritionInfo(155, 22, 15, 9),
                            discountedPrice = 15.83,
                            category = listOf(),
                            dateAdded = Clock.System.now()
                                .toLocalDateTime(TimeZone.currentSystemDefault()),
                            popularityIndex = 392
                        )
                    )
                }
            LocationPermissionScreen(koinViewModel<AddressViewModel>())
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