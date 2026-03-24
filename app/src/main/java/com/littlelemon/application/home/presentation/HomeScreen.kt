package com.littlelemon.application.home.presentation

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
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var currentDestination: NavigationOption by remember { mutableStateOf(NavigationOption.HOME) }
    val backStack = rememberNavBackStack(HomeRoute)

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

//    Text("Home Screen")
//
//    val backStack = rememberNavBackStack(Route1)
//
//
//    val premiumSpring = spring<IntOffset>(
//        dampingRatio = 0.85f, // Slight glide, almost no bounce
//        stiffness = Spring.StiffnessLow // Slower, relaxed movement
//    )
//    val fadeSpec = tween<Float>(durationMillis = 300)
//
//    val animationSpec = NavDisplay.transitionSpec {
//        // Slide new content up, keeping the old content in place underneath
//        slideInVertically(
//            initialOffsetY = { it },
//            animationSpec = tween(1000)
//        ) togetherWith ExitTransition.KeepUntilTransitionsFinished
//    } + NavDisplay.popTransitionSpec {
//        // Slide old content down, revealing the new content in place underneath
//        EnterTransition.None togetherWith
//                slideOutVertically(
//                    targetOffsetY = { it },
//                    animationSpec = tween(1000)
//                )
//    } + NavDisplay.predictivePopTransitionSpec {
//        // Slide old content down, revealing the new content in place underneath
//        EnterTransition.None togetherWith
//                slideOutVertically(
//                    targetOffsetY = { it },
//                    animationSpec = tween(1000)
//                )
//    }
//
//
//    NavDisplay(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(40.dp),
//        backStack = backStack,
//        entryProvider = entryProvider {
//            entry<Route1>() {
//                Column(
//                    Modifier
//                        .fillMaxSize()
//                        .background(Color.Red),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Button(onClick = { backStack.add(Route2) }) {
//                        Text("Navigate to next screen")
//                    }
//                }
//            }
//            entry<Route2>() {
//                Column(
//                    Modifier
//                        .fillMaxSize()
//                        .background(Color.Green),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Button(onClick = { backStack.add(Route3) }) {
//                        Text("Navigate to next screen")
//                    }
//                    Button(onClick = { backStack.removeLastOrNull() }) {
//                        Text("Navigate back")
//                    }
//                }
//            }
//            entry<Route3>() {
//                Column(
//                    Modifier
//                        .fillMaxSize()
//                        .background(Color.Yellow),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//
//                    Button(onClick = { backStack.removeLastOrNull() }) {
//                        Text("Navigate back")
//                    }
//                }
//            }
//        },
//        transitionSpec = {
//            // 1. Define the premium, subtle spring physics
//            val premiumSpring = spring<IntOffset>(
//                dampingRatio = 0.75f, // Slight glide, almost no bounce
//                stiffness = Spring.StiffnessLow // Slower, relaxed movement
//            )
//            val fadeSpec = tween<Float>(durationMillis = 250)
//
//            // 2. Navigating FORWARD (Login -> Verify)
//            // New screen slides UP from bottom. Old screen slides UP to top.
//            (slideInVertically(
//                animationSpec = premiumSpring,
//                initialOffsetY = { fullHeight -> fullHeight }
//            ) + fadeIn(animationSpec = fadeSpec)).togetherWith(
//                slideOutVertically(
//                    animationSpec = premiumSpring,
//                    targetOffsetY = { fullHeight -> -fullHeight / 3 } // Parallax effect: moves slower than entering screen
//                ) + fadeOut(animationSpec = fadeSpec)
//            )
//        },
//        popTransitionSpec = {
//            // 3. Navigating BACKWARDS (Verify -> Login)
//            // New screen slides DOWN from top. Old screen slides DOWN to bottom.
//            val premiumSpring = spring<IntOffset>(
//                dampingRatio = 0.75f,
//                stiffness = Spring.StiffnessLow
//            )
//            val fadeSpec = tween<Float>(durationMillis = 250)
//
//            (slideInVertically(
//                animationSpec = premiumSpring,
//                initialOffsetY = { fullHeight -> -fullHeight / 3 } // Parallax effect
//            ) + fadeIn(animationSpec = fadeSpec)).togetherWith(
//                slideOutVertically(
//                    animationSpec = premiumSpring,
//                    targetOffsetY = { fullHeight -> fullHeight }
//                ) + fadeOut(animationSpec = fadeSpec)
//            )
//        }
//    )
}

@Preview
@Composable
private fun HomeScreenPreview() {
    LittleLemonTheme() {
        HomeScreen()
    }
}