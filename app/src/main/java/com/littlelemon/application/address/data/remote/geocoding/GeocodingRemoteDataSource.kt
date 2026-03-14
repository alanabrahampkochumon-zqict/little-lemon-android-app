package com.littlelemon.application.address.data.remote.geocoding

import com.littlelemon.application.address.data.remote.models.GeocodingDTO

interface GeocodingRemoteDataSource {
    suspend fun geocodeAddress(address: String): GeocodingDTO

    suspend fun reverseGeocodeAddress(latLng: GeocodingDTO.LatLng): GeocodingDTO
}