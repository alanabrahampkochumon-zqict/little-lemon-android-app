package com.littlelemon.application.cart.domain.usecase

import com.littlelemon.application.cart.domain.CartRepository
import com.littlelemon.application.cart.domain.models.CartItem

class UpsertCartItemUseCase(private val repository: CartRepository) {
    // TODO: Add tests
    suspend operator fun invoke(cartItem: CartItem) = repository.upsertCartItem(cartItem)
}