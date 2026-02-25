package com.littlelemon.application.home.presentation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable

// TODO: Remove playground code

@Serializable
data object Route1 : NavKey

@Serializable
data object Route2 : NavKey

@Serializable
data object Route3 : NavKey


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Text("Home Screen")

    val backStack = rememberNavBackStack(Route1)


    val premiumSpring = spring<IntOffset>(
        dampingRatio = 0.85f, // Slight glide, almost no bounce
        stiffness = Spring.StiffnessLow // Slower, relaxed movement
    )
    val fadeSpec = tween<Float>(durationMillis = 300)

    val animationSpec = NavDisplay.transitionSpec {
        // Slide new content up, keeping the old content in place underneath
        slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(1000)
        ) togetherWith ExitTransition.KeepUntilTransitionsFinished
    } + NavDisplay.popTransitionSpec {
        // Slide old content down, revealing the new content in place underneath
        EnterTransition.None togetherWith
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(1000)
                )
    } + NavDisplay.predictivePopTransitionSpec {
        // Slide old content down, revealing the new content in place underneath
        EnterTransition.None togetherWith
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(1000)
                )
    }


    NavDisplay(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Route1>() {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Red),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { backStack.add(Route2) }) {
                        Text("Navigate to next screen")
                    }
                }
            }
            entry<Route2>() {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Green),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { backStack.add(Route3) }) {
                        Text("Navigate to next screen")
                    }
                    Button(onClick = { backStack.removeLastOrNull() }) {
                        Text("Navigate back")
                    }
                }
            }
            entry<Route3>() {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Yellow),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Button(onClick = { backStack.removeLastOrNull() }) {
                        Text("Navigate back")
                    }
                }
            }
        },
        transitionSpec = {
            // 1. Define the premium, subtle spring physics
            val premiumSpring = spring<IntOffset>(
                dampingRatio = 0.75f, // Slight glide, almost no bounce
                stiffness = Spring.StiffnessLow // Slower, relaxed movement
            )
            val fadeSpec = tween<Float>(durationMillis = 250)

            // 2. Navigating FORWARD (Login -> Verify)
            // New screen slides UP from bottom. Old screen slides UP to top.
            (slideInVertically(
                animationSpec = premiumSpring,
                initialOffsetY = { fullHeight -> fullHeight }
            ) + fadeIn(animationSpec = fadeSpec)).togetherWith(
                slideOutVertically(
                    animationSpec = premiumSpring,
                    targetOffsetY = { fullHeight -> -fullHeight / 3 } // Parallax effect: moves slower than entering screen
                ) + fadeOut(animationSpec = fadeSpec)
            )
        },
        popTransitionSpec = {
            // 3. Navigating BACKWARDS (Verify -> Login)
            // New screen slides DOWN from top. Old screen slides DOWN to bottom.
            val premiumSpring = spring<IntOffset>(
                dampingRatio = 0.75f,
                stiffness = Spring.StiffnessLow
            )
            val fadeSpec = tween<Float>(durationMillis = 250)

            (slideInVertically(
                animationSpec = premiumSpring,
                initialOffsetY = { fullHeight -> -fullHeight / 3 } // Parallax effect
            ) + fadeIn(animationSpec = fadeSpec)).togetherWith(
                slideOutVertically(
                    animationSpec = premiumSpring,
                    targetOffsetY = { fullHeight -> fullHeight }
                ) + fadeOut(animationSpec = fadeSpec)
            )
        }
    )
}