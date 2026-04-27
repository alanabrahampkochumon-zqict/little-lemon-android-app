package com.littlelemon.application.cart.data

import com.littlelemon.application.cart.data.remote.CartRemoteDataSource
import com.littlelemon.application.cart.domain.CartRepository
import com.littlelemon.application.cart.domain.models.CartItem
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.database.cart.CartDao
import kotlinx.coroutines.flow.Flow

class DefaultCartRepository(
    val remoteDataSource: CartRemoteDataSource,
    val localDataSource: CartDao
) : CartRepository {

    override suspend fun upsertCartItem(cartItem: CartItem) {
        TODO("Not yet implemented")
    }


    override suspend fun clearCart() {
        TODO("Not yet implemented")
    }

    
    override fun getAllCartItems(): Flow<Resource<List<CartItem>>> {
        TODO("Not yet implemented")
    }


}