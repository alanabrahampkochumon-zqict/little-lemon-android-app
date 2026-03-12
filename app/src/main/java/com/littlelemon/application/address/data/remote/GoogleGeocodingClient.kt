package com.littlelemon.application.address.data.remote

import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi

class GoogleGeocodingClient(
    val context: GeoApiContext
) : GeocodingClient {
    override suspend fun geocode(address: String): String {
        return GeocodingApi.geocode(context, "").await().contentToString()
        TODO("Not yet implemented")
    }
}