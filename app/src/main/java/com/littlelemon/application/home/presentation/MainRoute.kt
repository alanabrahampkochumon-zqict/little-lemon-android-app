package com.littlelemon.application.home.presentation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface MainRoute : NavKey

@Serializable
object HomeRoute : MainRoute

@Serializable
object MenuRoute : MainRoute

@Serializable
object OrdersRoute : MainRoute

@Serializable
object CartRoute : MainRoute

@Serializable
object ProfileRoute : MainRoute