package com.littlelemon.application.cart.presentation

import com.littlelemon.application.shared.cart.domain.models.CartDetailItem


sealed interface CartAction {
    data class IncreaseQuantity(val cartDetailItem: CartDetailItem) : CartAction
    data class DecreaseQuantity(val cartDetailItem: CartDetailItem) : CartAction
    data class RemoveItem(val cartDetailItem: CartDetailItem) : CartAction
}