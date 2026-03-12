package com.littlelemon.application.address.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GeocodingDTO(
    val results: List<Result>,
    val status: String
) {
    @Serializable
    data class Result(
        val geometry: Geometry
    )

    @Serializable
    data class Location(
        val lat: Double,
        val lng: Double
    )

    @Serializable
    data class Geometry(
        val location: Location,
        @SerialName("location_type")
        val locationType: String,
    )
}