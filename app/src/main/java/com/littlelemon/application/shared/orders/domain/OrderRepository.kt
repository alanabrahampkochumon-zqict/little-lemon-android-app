package com.littlelemon.application.orders.domain

import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.orders.domain.models.CartItem
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getCartItems(): Flow<Resource<List<CartItem>>>
    suspend fun placeOrder(): Resource<Unit> // TODO: Change to order item(details)
    suspend fun cancelOrder(): Resource<Unit> // TODO: Change to order item
    fun getAllOrders(): Flow<Resource<Unit>> // TODO: Change to OrderList with sorting option
    suspend fun updateCartQuantity(dishId: String, newQuantity: Int): Resource<Unit>
    suspend fun getCartItemCount(dishId: String): Resource<Unit>
    suspend fun clearCart(): Resource<Unit>
//    suspend fun

}