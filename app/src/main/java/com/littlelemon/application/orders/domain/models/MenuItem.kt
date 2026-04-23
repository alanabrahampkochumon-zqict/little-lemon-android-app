package com.littlelemon.application.orders.domain.models

import com.littlelemon.application.menu.domain.models.Dish

// TODO: Remove if not needed.
data class MenuItem(
    val dish: Dish,
    val quantity: Int = 1
)