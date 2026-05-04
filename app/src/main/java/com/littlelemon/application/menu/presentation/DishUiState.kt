package com.littlelemon.application.menu.presentation

import com.littlelemon.application.shared.menu.domain.models.Dish

data class DishUiState(
    val dish: Dish,
    val quantity: Int = 0
)