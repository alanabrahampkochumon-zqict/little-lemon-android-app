package com.littlelemon.application.cart.data.remote

import com.littlelemon.application.cart.data.remote.models.CartItemDTO
import com.littlelemon.application.core.data.remote.SupabaseRPC
import com.littlelemon.application.core.data.remote.SupabaseTables
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.github.jan.supabase.postgrest.postgrest
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class SupabaseCartRemoteDataSource(private val client: SupabaseClient) : CartRemoteDataSource {

    @Throws(
        PostgrestRestException::class,
        HttpRequestTimeoutException::class,
        HttpRequestException::class
    )
    override suspend fun updateCart(cartItem: CartItemDTO): CartItemDTO? {
        return client.postgrest.rpc(SupabaseRPC.UpdateCart.RPC_NAME, buildJsonObject {
            put(SupabaseRPC.UpdateCart.P_DISH_ID, cartItem.dishId)
            put(SupabaseRPC.UpdateCart.P_QUANTITY, cartItem.quantity)
        }).decodeSingleOrNull<CartItemDTO>()
    }


    @Throws(
        PostgrestRestException::class,
        HttpRequestTimeoutException::class,
        HttpRequestException::class
    )
    override suspend fun clearCart() {
        client.postgrest.rpc(SupabaseRPC.ClearCart.RPC_NAME)
    }

    override suspend fun getCart(): List<CartItemDTO> {
        return client.postgrest.from(SupabaseTables.CART).select().decodeList()
    }
}