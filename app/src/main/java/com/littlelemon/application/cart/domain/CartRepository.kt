package com.littlelemon.application.cart.domain

import com.littlelemon.application.cart.domain.models.CartItem
import com.littlelemon.application.core.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface CartRepository {

    val errorMessages: SharedFlow<String>

    suspend fun upsertCartItem(
        cartItem: CartItem
    )

    suspend fun clearCart(): Resource<Unit>

    fun getAllCartItems(): Flow<List<CartItem>>
}