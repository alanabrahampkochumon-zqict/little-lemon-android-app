package com.littlelemon.application.reservation.data.remote

import com.littlelemon.application.core.data.remote.SupabaseTables
import com.littlelemon.application.reservation.data.remote.models.ReservationDTO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.ktor.client.plugins.HttpRequestTimeoutException

class SupabaseReservationRemoteDataSource(
    private val client: SupabaseClient
) : ReservationRemoteDataSource {

    @Throws(
        PostgrestRestException::class,
        HttpRequestTimeoutException::class,
        HttpRequestException::class
    )
    override suspend fun getReservations(): List<ReservationDTO> {
        return client.from(SupabaseTables.RESERVATIONS)
            .select(columns = Columns.list("id, reservation_time, party_size, status, special_instructions"))
            .decodeList<ReservationDTO>()
    }

    override suspend fun makeReservation(reservation: ReservationDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun cancelReservation(reservation: ReservationDTO) {
        TODO("Not yet implemented")
    }
}