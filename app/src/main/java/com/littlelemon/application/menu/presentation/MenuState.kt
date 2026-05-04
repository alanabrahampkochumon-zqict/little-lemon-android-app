package com.littlelemon.application.menu.presentation

import com.littlelemon.application.core.presentation.UiText

data class MenuState(
    val dishes: List<DishUiState>? = null,
    val error: UiText? = null,
    val isLoading: Boolean = false
)