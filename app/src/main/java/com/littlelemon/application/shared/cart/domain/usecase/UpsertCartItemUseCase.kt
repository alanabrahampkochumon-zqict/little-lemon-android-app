package com.littlelemon.application.shared.cart.domain.usecase

import com.littlelemon.application.shared.cart.domain.CartRepository
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem

class UpsertCartItemUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(cartDetailItem: CartDetailItem) =
        repository.upsertCartItem(cartDetailItem)
}