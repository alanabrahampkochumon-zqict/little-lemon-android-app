package com.littlelemon.application.cart.data.remote

import com.littlelemon.application.cart.data.remote.models.CartItemDTO

interface CartRemoteDataSource {

    suspend fun updateCart(cartItem: CartItemDTO): CartItemDTO?
    suspend fun clearCart()

    suspend fun getCart(): List<CartItemDTO>
}