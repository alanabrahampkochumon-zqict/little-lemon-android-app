package com.littlelemon.application.cart.data.remote

interface CartRemoteDataSource {

    suspend fun addToCart()

    suspend fun removeFromCart()

    suspend fun clearCart()
}