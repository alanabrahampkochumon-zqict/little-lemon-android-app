package com.littlelemon.application.reservation.data.remote.models

data class ReservationDTO(
    val id: String,
    val reservationTime: String,
    val people: Int,
    val status: String
)
