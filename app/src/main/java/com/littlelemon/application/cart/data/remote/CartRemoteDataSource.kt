package com.littlelemon.application.cart.data.remote

import com.littlelemon.application.cart.data.remote.models.CartItemDTO
import com.littlelemon.application.cart.domain.models.CartItem

interface CartRemoteDataSource {

    suspend fun updateCart(cartItem: CartItemDTO): CartItem?
    suspend fun clearCart()
}