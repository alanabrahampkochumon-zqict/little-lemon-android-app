package com.littlelemon.application.address.data.remote

import com.littlelemon.application.address.data.mappers.toResponse
import com.littlelemon.application.address.data.remote.models.AddressDTO
import com.littlelemon.application.address.data.remote.models.AddressRequestDTO
import com.littlelemon.application.core.data.remote.SupabaseTables
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.github.jan.supabase.postgrest.from
import io.ktor.client.plugins.HttpRequestTimeoutException

class AddressRemoteDataSourceImpl(
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
        return client.from(SupabaseTables.USER_ADDRESS).insert(address) { select() }
            .decodeSingle<AddressRequestDTO>().toResponse()
    }

    @Throws(
        PostgrestRestException::class,
        HttpRequestTimeoutException::class,
        HttpRequestException::class
    )
    override suspend fun updateAddress(address: AddressRequestDTO): AddressDTO {
        return client.from(SupabaseTables.USER_ADDRESS).update(address) { select() }
            .decodeSingle<AddressRequestDTO>().toResponse()
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