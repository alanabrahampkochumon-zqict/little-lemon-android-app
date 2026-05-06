package com.littlelemon.application.shared.cart.domain.usecase

import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.shared.cart.domain.CartRepository

class RefreshCartUseCase(private val repository: CartRepository) {

    suspend operator fun invoke(): Resource<Unit> = repository.refreshCart()
}