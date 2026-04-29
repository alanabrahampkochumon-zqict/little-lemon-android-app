package com.littlelemon.application.cart.domain.usecase

import com.littlelemon.application.cart.domain.CartRepository

class ClearCartUseCase(private val repository: CartRepository) {
    suspend operator fun invoke() = repository.clearCart()
}