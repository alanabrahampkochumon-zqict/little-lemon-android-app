package com.littlelemon.application.cart.presentation

import com.littlelemon.application.cart.domain.models.CartItem
import com.littlelemon.application.core.presentation.UiText

data class CartState(
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val cartItems: List<CartItem> = emptyList()
)
