package com.littlelemon.application.cart.presentation

import com.littlelemon.application.core.presentation.UiText
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem

data class CartState(
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val cartDetailItems: List<CartDetailItem> = emptyList()
)
