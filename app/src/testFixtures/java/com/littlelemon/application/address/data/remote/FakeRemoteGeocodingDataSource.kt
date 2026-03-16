package com.littlelemon.application.address.data.remote

import com.littlelemon.application.address.data.remote.geocoding.GeocodingRemoteDataSource
import com.littlelemon.application.address.data.remote.models.GeocodingDTO
import com.littlelemon.application.address.utils.GeocodingGenerator

class FakeRemoteGeocodingDataSource(
    private val throwError: Boolean = false,
) : GeocodingRemoteDataSource {
    override suspend fun geocodeAddress(address: String): GeocodingDTO {
        if (throwError) throw IllegalArgumentException()
        return GeocodingGenerator.generateGeocodingResult().second.copy(fullAddress = address)
    }

    override suspend fun reverseGeocodeAddress(latLng: GeocodingDTO.LatLng): GeocodingDTO {
        if (throwError) throw IllegalArgumentException()
        return GeocodingGenerator.generateGeocodingResult().second.copy(latLng = latLng)
    }
}