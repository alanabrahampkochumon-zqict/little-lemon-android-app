package com.littlelemon.application.cart.presentation

import com.littlelemon.application.cart.domain.models.CartItem


sealed interface CartAction {
    data class IncreaseQuantity(val cartItem: CartItem) : CartAction
    data class DecreaseQuantity(val cartItem: CartItem) : CartAction
    data class RemoveItem(val cartItem: CartItem) : CartAction
}