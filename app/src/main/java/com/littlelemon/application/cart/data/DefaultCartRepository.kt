package com.littlelemon.application.cart.data

import com.littlelemon.application.cart.CartConstants
import com.littlelemon.application.cart.data.remote.CartRemoteDataSource
import com.littlelemon.application.cart.domain.CartRepository
import com.littlelemon.application.cart.domain.models.CartItem
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.database.cart.CartDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DefaultCartRepository(
    val remoteDataSource: CartRemoteDataSource,
    val localDataSource: CartDao
) : CartRepository {


    private val cartJobs = mutableMapOf<String, Job>()

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())


    override suspend fun upsertCartItem(
        cartItem: CartItem,
        previousQuantity: Int
    ): Resource<Unit> {

        TODO()
        cartJobs[cartItem.id] = scope.launch {
            delay(CartConstants.NETWORK_DEBOUNCE)
//            remoteDataSource.updateCart(CartItemDTO(dishId, newQuantity))
        }
    }


    override suspend fun clearCart(): Resource<Unit> {
        TODO("Not yet implemented")
    }


    override fun getAllCartItems(): Flow<Resource<List<CartItem>>> {
        TODO("Not yet implemented")
    }


}