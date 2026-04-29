package com.littlelemon.application.cart.data

import com.littlelemon.application.cart.CartConstants
import com.littlelemon.application.cart.data.remote.CartRemoteDataSource
import com.littlelemon.application.cart.data.remote.mappers.toEntity
import com.littlelemon.application.cart.domain.CartRepository
import com.littlelemon.application.cart.domain.models.CartItem
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.database.cart.CartDao
import com.littlelemon.application.database.cart.mappers.toCartItems
import com.littlelemon.application.database.cart.mappers.toDTO
import com.littlelemon.application.database.cart.mappers.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DefaultCartRepository(
    private val remoteDataSource: CartRemoteDataSource,
    private val localDataSource: CartDao,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CartRepository {


    private val cartJobs = mutableMapOf<String, Job>()
    private val cartDefault = mutableMapOf<String, Int>()

    private val scope = CoroutineScope(dispatcher + SupervisorJob())


    override suspend fun upsertCartItem(
        cartItem: CartItem
    ) {
        val dishId = cartItem.dish.id
        try {
            // Check if a default value exists in the cache,
            // If not, update with a value from the cache
            if (!cartDefault.containsKey(dishId)) cartDefault[dishId] =
                localDataSource.getQuantity(dishId)

            // Update the database to reflect the updated cart quantity
            localDataSource.upsertCartItem(cartItem.toEntity())
        } catch (_: Exception) {
            currentCoroutineContext().ensureActive()
            // TODO: Message buffer impl
        }

        // Cancel any previous network jobs
        cartJobs[dishId]?.cancel()

        // Start a new job to update the remote DS
        cartJobs[dishId] = scope.launch {
            try {
                delay(CartConstants.NETWORK_DEBOUNCE)
                remoteDataSource.updateCart(cartItem.toDTO())
            } catch (_: Exception) {
                currentCoroutineContext().ensureActive()
                // Removing when the cart item is zero is handled by the DAO
                localDataSource.upsertOrRemoveCartItem(
                    cartItem.copy(
                        quantity = cartDefault[dishId] ?: 0
                    ).toEntity()
                )
            } finally {
                // Invalidate the cache
                cartDefault.remove(dishId)
                cartJobs.remove(dishId)
            }
        }

    }


    override suspend fun clearCart(): Resource<Unit> {
        try {
            remoteDataSource.clearCart()
            localDataSource.clearCartItems()
        } catch (_: Exception) {
            return Resource.Failure(errorMessage = CartErrorMessages.ERROR_CLEARING_CART)
        }
        return Resource.Success()
    }

    override fun getAllCartItems(): Flow<List<CartItem>> =
        localDataSource.getAllCartItems().map { cartItemDetails ->
            cartItemDetails.toCartItems()
        }.onStart {
            try {
                val remoteCart = remoteDataSource.getCart().map { it.toEntity() }
                if (remoteCart.isEmpty()) {
                    localDataSource.clearCartItems()
                } else {
                    remoteCart.forEach { localDataSource.upsertCartItem(it) }
                }
            } catch (_: Exception) {
                currentCoroutineContext().ensureActive()
                // TODO: Broadcast to your error event bus (SharedFlow)
            }
        }.catch {
            // TODO: Broadcast to your error event bus (SharedFlow)
        }


}