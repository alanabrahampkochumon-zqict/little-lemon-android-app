package com.littlelemon.application.reservation.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ReservationDTO(
    val id: String?,
    @SerialName("reservation_time")
    val reservationTime: String,
    @SerialName("party_size")
    val people: Int,
    val status: String?,
    @SerialName("special_instructions")
    val specialInstructions: String
)
