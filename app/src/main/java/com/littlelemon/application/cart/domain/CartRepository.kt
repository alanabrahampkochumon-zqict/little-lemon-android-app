package com.littlelemon.application.cart.domain

import com.littlelemon.application.cart.domain.models.CartItem
import com.littlelemon.application.core.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    suspend fun upsertCartItem(
        cartItem: CartItem,
        previousQuantity: Int
    ): Resource<Unit>

    suspend fun clearCart(): Resource<Unit>

    fun getAllCartItems(): Flow<Resource<List<CartItem>>>
}