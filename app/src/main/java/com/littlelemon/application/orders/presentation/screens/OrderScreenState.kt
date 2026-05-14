package com.littlelemon.application.orders.presentation.screens

import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.core.presentation.UiText
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem

data class OrderScreenState(
    val cartItems: List<CartDetailItem> = emptyList(),
    val isCartItemLoading: Boolean = false,
    val cartItemError: UiText? = null,

    val defaultAddress: LocalAddress? = null,
    val isAddressLoading: Boolean = false,
    val addressError: UiText? = null
)