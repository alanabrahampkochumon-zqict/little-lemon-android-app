package com.littlelemon.application.address.data.local.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GeocodingEntity(
    @PrimaryKey
    val placeId: String,
    @Embedded(prefix = "loc")
    val latLng: LatLng,
    val locationType: LocationType,
    val partialMatch: Boolean,
    val fullAddress: String,
    @Embedded(prefix = "addr_")
    val address: Address?,
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