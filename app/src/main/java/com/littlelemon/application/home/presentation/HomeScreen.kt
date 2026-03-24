package com.littlelemon.application.home.presentation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors


// TODO: Add cardinality to animation
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var currentDestination: NavigationOption by remember { mutableStateOf(NavigationOption.HOME) }
    val backStack = rememberNavBackStack(HomeRoute)

    val premiumSpring = spring<IntOffset>(
        dampingRatio = 0.85f, // Slight glide, almost no bounce
        stiffness = Spring.StiffnessLow // Slower, relaxed movement
    )

    Scaffold(
        bottomBar = {
            BottomNavigation(
                { newDestination ->
                    currentDestination = newDestination
                    backStack.clear()
                    when (newDestination) {
                        NavigationOption.HOME -> backStack.add(HomeRoute)
                        NavigationOption.MENU -> backStack.add(MenuRoute)
                        NavigationOption.ORDER -> backStack.add(OrdersRoute)
                        NavigationOption.CART -> backStack.add(CartRoute)
                        NavigationOption.PROFILE -> backStack.add(ProfileRoute)
                    }
                },
                selected = currentDestination
            )
        },
        topBar = {
            TopAppBar({/* TODO(Implementation) */ })
        },
        containerColor = MaterialTheme.colors.secondary,
        modifier = Modifier
            .fillMaxWidth()
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.padding(innerPadding),
            transitionSpec = {
                // Slide in from right when navigating forward
                slideInHorizontally(initialOffsetX = { it }) togetherWith
                        slideOutHorizontally(targetOffsetX = { -it })
            },
            popTransitionSpec = {
                // Slide in from left when navigating back
                slideInHorizontally(initialOffsetX = { -it }) togetherWith
                        slideOutHorizontally(targetOffsetX = { it })
            },
            predictivePopTransitionSpec = {
                // Slide in from left when navigating back
                slideInHorizontally(initialOffsetX = { -it }) togetherWith
                        slideOutHorizontally(targetOffsetX = { it })
            },
            entryProvider = { entry ->
                when (entry) {
                    is HomeRoute -> NavEntry(entry) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .background(Color.LightGray)
                                .fillMaxSize()
                        ) {
                            Text("Home")
                        }
                    }

                    is MenuRoute -> NavEntry(entry) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text("Menu")
                        }
                    }

                    is OrdersRoute -> NavEntry(entry) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .background(Color.LightGray)
                                .fillMaxSize()
                        ) {
                            Text("Orders")
                        }
                    }

                    is CartRoute -> NavEntry(entry) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text("Cart")
                        }
                    }

                    is ProfileRoute -> NavEntry(entry) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .background(Color.LightGray)
                                .fillMaxSize()
                        ) {
                            Text("Profile")
                        }
                    }

                    else -> NavEntry(entry) { Text("UnknownRoute") }
                }
            })
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    LittleLemonTheme() {
        HomeScreen()
    }
}