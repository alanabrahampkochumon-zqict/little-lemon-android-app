package com.littlelemon.application.shared.cart.domain.models

import com.littlelemon.application.shared.menu.domain.models.Dish

data class CartItem(
    val dish: Dish,
    val quantity: Int = 1
)