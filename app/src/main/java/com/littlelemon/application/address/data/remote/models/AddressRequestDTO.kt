package com.littlelemon.application.address.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressRequestDTO(
    val id: String?,
    val label: String?,
    val address: String?,
    @SerialName("street_address")
    val streetAddress: String?,
    val city: String?,
    val state: String?,
    @SerialName("pin_code")
    val pinCode: String?,
    val location: String?,
    @SerialName("created_at")
    val createdAt: Long,
)
