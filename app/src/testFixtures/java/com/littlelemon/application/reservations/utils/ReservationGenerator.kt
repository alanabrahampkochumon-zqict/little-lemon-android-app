package com.littlelemon.application.reservations.utils

import com.littlelemon.application.reservation.data.remote.models.ReservationDTO
import com.littlelemon.application.utils.LocalDateTimeGenerator
import io.github.serpro69.kfaker.faker
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
object ReservationGenerator {

    val faker = faker {}
    val RESERVATION_STATUSES = listOf("reserved", "upcoming", "cancelled")

    fun generateReservationDTO(): ReservationDTO {
        return ReservationDTO(
            id = Uuid.generateV4().toString(),
            reservationTime = LocalDateTimeGenerator.generateTimestampTZ().second,
            people = Random.nextInt(3, 7),
            status = RESERVATION_STATUSES.random(),
            specialInstructions = faker.random.randomString(Random.nextInt(20, 50))
        )
    }
}