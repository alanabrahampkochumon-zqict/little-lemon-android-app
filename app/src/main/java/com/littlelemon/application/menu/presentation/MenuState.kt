package com.littlelemon.application.menu.presentation

import com.littlelemon.application.core.presentation.UiText
import com.littlelemon.application.menu.domain.models.Dish

data class MenuState(
    val dishes: List<Dish>? = null,
    val error: UiText? = null,
    val isLoading: Boolean = false
)