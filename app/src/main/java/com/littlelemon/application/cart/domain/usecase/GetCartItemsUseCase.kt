package com.littlelemon.application.cart.domain.usecase

import com.littlelemon.application.cart.domain.CartRepository
import com.littlelemon.application.cart.domain.models.CartItem
import kotlinx.coroutines.flow.Flow

class GetCartItemsUseCase(
    private val repository: CartRepository
) {
    operator fun invoke(): Flow<List<CartItem>> = repository.getAllCartItems()
}