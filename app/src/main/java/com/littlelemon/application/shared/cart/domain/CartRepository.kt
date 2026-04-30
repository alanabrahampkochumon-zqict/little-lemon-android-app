package com.littlelemon.application.shared.cart.domain

import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface CartRepository {

    val errorMessages: SharedFlow<String>

    suspend fun upsertCartItem(
        cartDetailItem: CartDetailItem
    )

    suspend fun clearCart(): Resource<Unit>

    fun getAllCartItems(): Flow<List<CartDetailItem>>
}