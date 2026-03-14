package com.littlelemon.application.address.data.remote.geocoding

import com.google.maps.model.LatLng
import com.littlelemon.application.address.data.mappers.toGeocodingDTO
import com.littlelemon.application.address.data.remote.models.GeocodingDTO
import com.littlelemon.application.address.domain.GeocoderException
import com.littlelemon.application.core.domain.exceptions.InvalidRequestException
import com.littlelemon.application.core.domain.exceptions.RequestDeniedException
import com.littlelemon.application.core.domain.exceptions.UnknownException
import com.littlelemon.application.core.domain.utils.safeApiCall

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
    override suspend fun geocodeAddress(address: String): GeocodingDTO = safeApiCall {
        val result = engine.geocode(address)
        if (result.isEmpty())
            throw GeocoderException.ZeroResults("No matching results found for $address")
        result.first().toGeocodingDTO()
    }

    override suspend fun reverseGeocodeAddress(latLng: GeocodingDTO.LatLng) = safeApiCall {
        val result = engine.reverseGeocode(LatLng(latLng.lat, latLng.lng))
        if (result.isEmpty())
            throw GeocoderException.ZeroResults("No matching results found for (${latLng.lat}, ${latLng.lng})")
        result.first().toGeocodingDTO()
    }
}