package com.littlelemon.application.orders.presentation

import com.littlelemon.application.core.presentation.UiText
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem


data class OrderScreenState(
    val cartItems: List<CartDetailItem> = emptyList(),
    val isCartItemLoading: Boolean = false,
    val cartItemError: UiText? = null
)