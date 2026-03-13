package com.littlelemon.application.address.data.remote

import com.littlelemon.application.address.data.remote.models.GeocodingDTO

interface GeocodingClient {

    suspend fun geocode(address: String): GeocodingDTO

//    suspend fun reverseGeocode()
}