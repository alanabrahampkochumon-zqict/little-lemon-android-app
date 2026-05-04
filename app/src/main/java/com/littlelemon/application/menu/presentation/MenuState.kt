package com.littlelemon.application.menu.presentation

import com.littlelemon.application.core.presentation.UiText
import com.littlelemon.application.shared.menu.domain.models.Dish

data class MenuState(
    val dishesDepr: List<Dish>? = null,// TODO: Remove
    val dishes: List<DishUiState>? = null,
    val error: UiText? = null,
    val isLoading: Boolean = false
)