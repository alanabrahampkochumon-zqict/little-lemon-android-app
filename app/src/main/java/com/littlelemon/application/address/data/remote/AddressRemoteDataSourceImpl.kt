package com.littlelemon.application.address.data.remote

import com.google.maps.GeoApiContext
import com.littlelemon.application.address.data.mappers.toResponse
import com.littlelemon.application.address.data.remote.models.AddressDTO
import com.littlelemon.application.address.data.remote.models.AddressRequestDTO
import com.littlelemon.application.address.domain.GeocoderException
import com.littlelemon.application.core.data.remote.SupabaseTables
import com.littlelemon.application.core.domain.exceptions.InvalidRequestException
import com.littlelemon.application.core.domain.exceptions.RequestDeniedException
import com.littlelemon.application.core.domain.exceptions.UnknownException
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AddressRemoteDataSourceImpl(
    private val client: SupabaseClient,
    private val geocodingContext: GeoApiContext
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
            "upsert_address",
            buildJsonObject {
                put("arg_location", address.location)
                put("arg_is_default", address.isDefault)
                put("arg_id", address.id)
                put("arg_label", address.label)
                put("arg_building_name", address.address)
                put("arg_street_address", address.streetAddress)
                put("arg_city", address.city)
                put("arg_state", address.state)
                put("arg_pin_code", address.pinCode)
                put("arg_created_at", address.createdAt)
            }
        ).decodeSingle<AddressRequestDTO>().toResponse()
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

    @Throws(
        GeocoderException.ZeroResults::class,
        GeocoderException.DailyLimit::class,
        GeocoderException.QueryLimit::class,
        RequestDeniedException::class,
        InvalidRequestException::class,
        UnknownException::class,
    )
    override suspend fun geocodeAddress(address: AddressRequestDTO) {
//        val response = GeocodingApi.geocode(context)
        TODO("Not yet implemented")
    }

}