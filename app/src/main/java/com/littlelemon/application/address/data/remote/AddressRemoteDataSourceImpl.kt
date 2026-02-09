package com.littlelemon.application.address.data.remote

import com.littlelemon.application.address.data.remote.models.AddressDTO
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
    override suspend fun saveAddress(address: AddressDTO): AddressDTO {
        TODO("Implementation")
    }

    override suspend fun deleteAddress(address: AddressDTO): AddressDTO {
        TODO("Not yet implemented")
    }

    override suspend fun updateAddress(address: AddressDTO): AddressDTO {
        TODO("Not yet implemented")
    }
}