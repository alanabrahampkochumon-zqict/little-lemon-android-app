package com.littlelemon.application.address.data.remote.models


data class GeocodingDTO(
    val latLng: LatLng,
    val locationType: LocationType,
    val partialMatch: Boolean
) {
    data class LatLng(
        val lat: Double,
        val lng: Double
    )

    enum class LocationType {
        ROOFTOP,
        RANGE_INTERPOLATED,
        GEOMETRIC_CENTER,
        APPROXIMATE,
        UNKNOWN
    }
}