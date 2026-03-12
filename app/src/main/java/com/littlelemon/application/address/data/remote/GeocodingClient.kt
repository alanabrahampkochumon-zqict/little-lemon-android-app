package com.littlelemon.application.address.data.remote

interface GeocodingClient {

    suspend fun geocode(address: String): String

//    suspend fun reverseGeocode()
}