package com.littlelemon.application.shared.cart.domain.models

data class CartItem(
    val dishId: String,
    val quantity: Int = 0,
)