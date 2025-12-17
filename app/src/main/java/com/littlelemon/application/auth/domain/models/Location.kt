package com.littlelemon.application.auth.domain.models

data class Location(
    val latitude: Double,
    val longitude: Double,
    // Address is nullable because geocoding might fail or be offline
    val address: String? = null
)
