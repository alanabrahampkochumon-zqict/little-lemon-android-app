package com.littlelemon.application.reservation.data.remote

import com.littlelemon.application.reservation.data.remote.models.ReservationDTO

class SupabaseReservationRemoteDataSource : ReservationRemoteDataSource {
    override suspend fun getReservations(): List<ReservationDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun makeReservation(reservation: ReservationDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun cancelReservation(reservation: ReservationDTO) {
        TODO("Not yet implemented")
    }
}