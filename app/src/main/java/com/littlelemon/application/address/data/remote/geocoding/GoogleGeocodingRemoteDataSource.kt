package com.littlelemon.application.address.data.remote.geocoding

import com.littlelemon.application.address.data.remote.models.GeocodingDTO
import com.littlelemon.application.address.domain.GeocoderException
import com.littlelemon.application.core.domain.exceptions.InvalidRequestException
import com.littlelemon.application.core.domain.exceptions.RequestDeniedException
import com.littlelemon.application.core.domain.exceptions.UnknownException

class GoogleGeocodingRemoteDataSource(
    private val engine: GeocodingEngine
) : GeocodingRemoteDataSource {
    @Throws(
        GeocoderException.ZeroResults::class,
        GeocoderException.DailyLimit::class,
        GeocoderException.QueryLimit::class,
        RequestDeniedException::class,
        InvalidRequestException::class,
        UnknownException::class,
    )
    override suspend fun geocodeAddress(address: String): GeocodingDTO {
//        val response = GeocodingApi.geocode(context)
        TODO("Not yet implemented")
    }
}