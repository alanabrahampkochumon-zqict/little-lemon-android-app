package com.littlelemon.application.reservation.data.remote

import com.littlelemon.application.reservation.data.remote.models.ReservationDTO
import io.github.jan.supabase.SupabaseClient

class SupabaseReservationRemoteDataSource(
    private val client: SupabaseClient
) : ReservationRemoteDataSource {
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