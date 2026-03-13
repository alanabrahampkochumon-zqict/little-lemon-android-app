package com.littlelemon.application.address.data.remote.geocoding

import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.model.GeocodingResult
import com.google.maps.model.LatLng

class GoogleGeocodingEngine(
    val context: GeoApiContext
) : GeocodingEngine {
    override suspend fun geocode(address: String): Array<GeocodingResult> {
        return GeocodingApi.geocode(context, address).await()
    }

    override suspend fun reverseGeocode(latLng: LatLng): Array<GeocodingResult> {
        return GeocodingApi.reverseGeocode(context, latLng).await()
    }
}