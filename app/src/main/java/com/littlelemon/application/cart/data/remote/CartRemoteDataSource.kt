package com.littlelemon.application.cart.data.remote

import com.littlelemon.application.cart.data.remote.models.CartItemDTO

interface CartRemoteDataSource {

    suspend fun addToCart(cartItem: CartItemDTO)

    suspend fun removeFromCart(cartItem: CartItemDTO)

    suspend fun clearCart()
}