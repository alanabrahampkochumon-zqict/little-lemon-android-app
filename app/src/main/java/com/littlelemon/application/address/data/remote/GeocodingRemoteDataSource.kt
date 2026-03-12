package com.littlelemon.application.address.data.remote

import com.littlelemon.application.address.data.remote.models.GeocodingDTO

interface GeocodingRemoteDataSource {
    suspend fun geocodeAddress(address: String): GeocodingDTO

    //TODO:
//    suspend fun reverseGeocodeAddress(latLng: LatLng)
}