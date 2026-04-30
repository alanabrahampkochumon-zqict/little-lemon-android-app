package com.littlelemon.application.shared.cart.domain.usecase

import com.littlelemon.application.shared.cart.domain.CartRepository

class ClearCartUseCase(private val repository: CartRepository) {
    suspend operator fun invoke() = repository.clearCart()
}