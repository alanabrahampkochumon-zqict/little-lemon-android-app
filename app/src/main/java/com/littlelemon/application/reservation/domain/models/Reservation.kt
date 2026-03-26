package com.littlelemon.application.reservation.domain.models

import kotlinx.datetime.LocalDateTime

data class Reservation(
    val reservationDate: LocalDateTime,
    val reservedDate: LocalDateTime,
    val reservationStatus: ReservationStatus,
    val reservedFor: Int
) {
    enum class ReservationStatus {
        Upcoming,
        Expired,
        Cancelled
    }
}
