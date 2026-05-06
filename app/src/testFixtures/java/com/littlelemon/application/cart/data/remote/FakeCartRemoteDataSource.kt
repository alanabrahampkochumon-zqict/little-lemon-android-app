package com.littlelemon.application.cart.data.remote

import com.littlelemon.application.shared.cart.data.remote.CartRemoteDataSource
import com.littlelemon.application.shared.cart.data.remote.models.CartItemDTO
import com.littlelemon.application.shared.cart.data.remote.models.CartSummaryDTO
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


// NOTE: Keep initial items null to seed the database
//       and pass in a emptyList to seed with no entries.
@OptIn(ExperimentalUuidApi::class)
class FakeCartRemoteDataSource(
    initialData: List<CartItemDTO>? = null,
    private val throwError: Boolean = false
) : CartRemoteDataSource {

    val remoteData = mutableListOf<CartItemDTO>()

    init {
        if (initialData == null) {
            val seedCount = Random.nextInt(5, 10)
            repeat(seedCount) {
                remoteData.add(CartItemDTO(Uuid.generateV4().toString(), Random.nextInt(5, 10)))
            }
        } else {
            remoteData.addAll(initialData)
        }
    }

    override suspend fun updateCart(cartItem: CartItemDTO): CartItemDTO? {
        if (throwError)
            throw IllegalArgumentException()
        if (cartItem in remoteData)
            remoteData.remove(cartItem)
        remoteData.add(cartItem)
        return cartItem
    }

    override suspend fun clearCart() {
        if (throwError)
            throw IllegalArgumentException()
        remoteData.clear()
    }

    override suspend fun getCart(): List<CartItemDTO> {
        if (throwError) throw IllegalArgumentException()
        return remoteData
    }

    override suspend fun refreshCart() {
        if (throwError) throw IllegalArgumentException()
        // NO-OP
    }

    override suspend fun getCartSummary(): CartSummaryDTO {
        if (throwError)
            throw IllegalArgumentException()
        return CartSummaryDTO(
            150, 15, 10, 0, 135
        )
    }

}