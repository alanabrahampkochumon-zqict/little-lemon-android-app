package com.littlelemon.application.shared.cart.data.remote

import com.littlelemon.application.shared.cart.data.remote.models.CartItemDTO
import com.littlelemon.application.shared.cart.data.remote.models.CartSummaryDTO

interface CartRemoteDataSource {

    suspend fun updateCart(cartItem: CartItemDTO): CartItemDTO?
    suspend fun clearCart()

    suspend fun getCart(): List<CartItemDTO>

    suspend fun getCartSummary(): CartSummaryDTO
}