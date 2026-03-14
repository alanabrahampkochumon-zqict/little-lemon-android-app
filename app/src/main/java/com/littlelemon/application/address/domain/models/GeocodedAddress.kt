package com.littlelemon.application.address.domain.models

data class GeocodedAddress(
    val id: String,
    val partialMatch: Boolean,
    val fullAddress: String?,
    val address: PhysicalAddress?,
    val location: LocalLocation?
)