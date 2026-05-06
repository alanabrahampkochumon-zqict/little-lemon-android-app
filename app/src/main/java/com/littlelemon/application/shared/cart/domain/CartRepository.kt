package com.littlelemon.application.shared.cart.domain

import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem
import com.littlelemon.application.shared.cart.domain.models.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface CartRepository {

    val errorMessages: SharedFlow<String>

    suspend fun upsertCartItem(
        cartDetailItem: CartDetailItem
    )

    suspend fun clearCart(): Resource<Unit>

    fun getAllDetailedCartItems(): Flow<List<CartDetailItem>>

    fun getAllCartItems(): Flow<List<CartItem>>

    fun refreshCart(): Resource<Unit>
}