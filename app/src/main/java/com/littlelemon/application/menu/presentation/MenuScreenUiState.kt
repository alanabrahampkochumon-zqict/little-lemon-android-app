package com.littlelemon.application.menu.presentation

import com.littlelemon.application.shared.menu.domain.models.Dish

data class MenuScreenUiState(
    val dish: Dish,
    val quantity: Int = 0
)