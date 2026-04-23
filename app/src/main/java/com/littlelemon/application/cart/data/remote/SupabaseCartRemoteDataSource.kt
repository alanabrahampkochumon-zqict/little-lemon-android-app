package com.littlelemon.application.cart.data.remote

import io.github.jan.supabase.SupabaseClient

class SupabaseCartRemoteDataSource(private val client: SupabaseClient) : CartRemoteDataSource {
    override suspend fun addToCart() {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromCart() {
        TODO("Not yet implemented")
    }

    override suspend fun clearCart() {
        TODO("Not yet implemented")
    }
}