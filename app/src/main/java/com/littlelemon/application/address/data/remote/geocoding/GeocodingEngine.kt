package com.littlelemon.application.address.data.remote.geocoding

import com.google.maps.model.GeocodingResult
import com.google.maps.model.LatLng

/**
 * A wrapper around Google's geocoding interface to facilitate unit testing remote data source.
 */
interface GeocodingEngine {
    suspend fun geocode(address: String): Array<GeocodingResult>
    suspend fun reverseGeocode(latLng: LatLng): Array<GeocodingResult>
}