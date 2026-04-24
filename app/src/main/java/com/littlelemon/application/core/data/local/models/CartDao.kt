package com.littlelemon.application.core.data.local.models

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Upsert
    suspend fun upsertCartItem(cartItem: CartItemEntity)

    @Query("DELETE FROM cartitementity WHERE id = :id")
    suspend fun deleteCartItem(id: String)

    @Transaction
    suspend fun upsertOrRemoveCartItem(cartItem: CartItemEntity) {
        upsertCartItem(cartItem)
        if (cartItem.quantity <= 0)
            deleteCartItem(cartItem.id)
    }

    @Query("SELECT * FROM cartitementity")
    fun getAllCartItems(): Flow<List<CartItemDetails>>
}