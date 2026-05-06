package com.littlelemon.application.shared.cart.data.remote

import com.littlelemon.application.core.data.remote.SupabaseRPC
import com.littlelemon.application.core.data.remote.SupabaseTables
import com.littlelemon.application.shared.cart.data.remote.models.CartItemDTO
import com.littlelemon.application.shared.cart.data.remote.models.CartSummaryDTO
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
        val res = client.postgrest.rpc(
            function = SupabaseRPC.UpdateCart.RPC_NAME,
            parameters = buildJsonObject {
                put("p_dish_id", cartItem.dishId)
                put("p_quantity", cartItem.quantity)
            }
        )
        return res.decodeAsOrNull<CartItemDTO>()
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

    // TODO: Move to order
    @Throws(
        PostgrestRestException::class,
        HttpRequestTimeoutException::class,
        HttpRequestException::class
    )
    override suspend fun getCartSummary(): CartSummaryDTO {
        return client.postgrest.rpc(SupabaseRPC.GetCartSummary.RPC_NAME).decodeSingle()
    }
}