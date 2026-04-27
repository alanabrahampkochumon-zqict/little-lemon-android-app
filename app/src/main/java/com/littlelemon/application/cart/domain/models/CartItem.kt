package com.littlelemon.application.cart.domain.models

import com.littlelemon.application.menu.domain.models.Dish

data class CartItem(
    val id: String,
    val dish: Dish,
    val quantity: Int = 1
)