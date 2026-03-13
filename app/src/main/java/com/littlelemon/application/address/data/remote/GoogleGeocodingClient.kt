package com.littlelemon.application.address.data.remote

import com.google.maps.GeoApiContext
import com.littlelemon.application.address.data.remote.models.GeocodingDTO

class GoogleGeocodingClient(
    val context: GeoApiContext
) : GeocodingClient {
    override suspend fun geocode(address: String): GeocodingDTO {
        TODO("Not yet implemented")
    }
}