package com.littlelemon.application.address.data.remote.models


data class GeocodingDTO(
    val latLng: LatLng,
    val locationType: LocationType,
    val partialMatch: Boolean,
    val fullAddress: String,
    val address: Address?,
    val placeId: String?
) {
    data class LatLng(
        val lat: Double,
        val lng: Double
    )

    data class Address(
        val address: String?,
        val streetAddress: String?,
        val city: String?,
        val state: String?,
        val country: String?,
        val pinCode: String?,
    )

    enum class LocationType {
        ROOFTOP,
        RANGE_INTERPOLATED,
        GEOMETRIC_CENTER,
        APPROXIMATE,
        UNKNOWN
    }
}