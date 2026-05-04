package com.littlelemon.application.shared.cart.domain.usecase

import com.littlelemon.application.shared.cart.domain.CartRepository
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem
import kotlinx.coroutines.flow.Flow

class GetCartItemDetailsUseCase(
    private val repository: CartRepository
) {
    operator fun invoke(): Flow<List<CartDetailItem>> = repository.getAllDetailedCartItems()
}