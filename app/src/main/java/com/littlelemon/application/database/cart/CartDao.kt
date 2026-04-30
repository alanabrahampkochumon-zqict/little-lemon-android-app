package com.littlelemon.application.database.cart

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.littlelemon.application.database.cart.models.CartItemDetails
import com.littlelemon.application.database.cart.models.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Upsert
    suspend fun upsertCartItem(cartItem: CartItemEntity)

    @Query("DELETE FROM cartitementity WHERE dishId = :dishId")
    suspend fun removeCartItem(dishId: String)

    @Transaction
    suspend fun upsertOrRemoveCartItem(cartItem: CartItemEntity) {
        upsertCartItem(cartItem)
        if (cartItem.quantity <= 0)
            removeCartItem(cartItem.dishId)
    }

    @Transaction
    @Query("SELECT * FROM cartitementity")
    fun getAllCartItemDetails(): Flow<List<CartItemDetails>>


    @Query("SELECT * FROM cartitementity")
    fun getAllCartItems(): Flow<List<CartItemEntity>>


    @Query("DELETE FROM cartitementity")
    fun clearCartItems()

    @Query("SELECT quantity FROM cartitementity WHERE dishId = :dishId")
    suspend fun getQuantity(dishId: String): Int
}