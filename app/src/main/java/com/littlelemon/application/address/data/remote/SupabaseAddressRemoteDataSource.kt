package com.littlelemon.application.address.data.remote

import com.littlelemon.application.address.data.remote.models.AddressDTO
import com.littlelemon.application.address.data.remote.models.AddressRequestDTO
import com.littlelemon.application.core.data.remote.SupabaseRPC
import com.littlelemon.application.core.data.remote.SupabaseTables
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class SupabaseAddressRemoteDataSource(
    private val client: SupabaseClient
) : AddressRemoteDataSource {

    @Throws(
        PostgrestRestException::class,
        HttpRequestTimeoutException::class,
        HttpRequestException::class
    )
    override suspend fun getAddress(): List<AddressDTO> {
        return client.from(SupabaseTables.USER_ADDRESS_VIEW).select().decodeList()
    }

    @Throws(
        PostgrestRestException::class,
        HttpRequestTimeoutException::class,
        HttpRequestException::class
    )
    override suspend fun saveAddress(address: AddressRequestDTO): AddressDTO {
        return client.postgrest.rpc(
            SupabaseRPC.UpsertAddress.RPC_NAME, buildJsonObject {
                put(SupabaseRPC.UpsertAddress.P_LOCATION, address.location)
                put(SupabaseRPC.UpsertAddress.P_CREATED_AT, address.createdAt.toString())
                put(SupabaseRPC.UpsertAddress.P_IS_DEFAULT, address.isDefault)
                put(SupabaseRPC.UpsertAddress.P_ID, address.id)
                put(SupabaseRPC.UpsertAddress.P_LABEL, address.label)
                put(SupabaseRPC.UpsertAddress.P_BUILDING_NAME, address.address)
                put(SupabaseRPC.UpsertAddress.P_STREET_ADDRESS, address.streetAddress)
                put(SupabaseRPC.UpsertAddress.P_CITY, address.city)
                put(SupabaseRPC.UpsertAddress.P_STATE, address.state)
                put(SupabaseRPC.UpsertAddress.P_PIN_CODE, address.pinCode)
            }).decodeSingle<AddressDTO>()
    }

    @Throws(
        PostgrestRestException::class,
        HttpRequestTimeoutException::class,
        HttpRequestException::class
    )
    override suspend fun deleteAddress(address: AddressRequestDTO) {
        if (address.id == null) return // Won't hit this case unless as id is only null for fresh addresses coming from VM
        client.from(SupabaseTables.USER_ADDRESS).delete { filter { eq("id", address.id) } }
    }

}