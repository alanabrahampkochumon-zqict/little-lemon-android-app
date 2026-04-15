package com.littlelemon.application.reservation.data.remote

import com.littlelemon.application.reservation.data.remote.models.ReservationDTO

interface ReservationRemoteDataSource {

    suspend fun getReservations(): List<ReservationDTO>

    suspend fun makeReservation(reservation: ReservationDTO): ReservationDTO

    suspend fun cancelReservation(reservation: ReservationDTO): ReservationDTO

}