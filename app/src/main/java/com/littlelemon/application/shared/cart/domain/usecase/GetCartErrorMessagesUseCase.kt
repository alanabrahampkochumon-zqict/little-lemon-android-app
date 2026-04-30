package com.littlelemon.application.shared.cart.domain.usecase

import com.littlelemon.application.shared.cart.domain.CartRepository
import kotlinx.coroutines.flow.SharedFlow

class GetCartErrorMessagesUseCase(private val repository: CartRepository) {
    operator fun invoke(): SharedFlow<String> = repository.errorMessages
}