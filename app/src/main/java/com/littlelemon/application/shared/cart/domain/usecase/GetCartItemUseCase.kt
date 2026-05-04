package com.littlelemon.application.shared.cart.domain.usecase

import com.littlelemon.application.shared.cart.domain.CartRepository
import com.littlelemon.application.shared.cart.domain.models.CartItem
import kotlinx.coroutines.flow.Flow

class GetCartItemUseCase(private val repository: CartRepository) {

    suspend operator fun invoke(): Flow<List<CartItem>> = TODO()
}