package com.littlelemon.application.shared.cart.domain.usecase

import com.littlelemon.application.shared.cart.domain.CartRepository
import com.littlelemon.application.shared.cart.domain.models.CartItem

class UpsertCartItemUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(cartItem: CartItem) = repository.upsertCartItem(cartItem)
}