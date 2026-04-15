package com.littlelemon.application.reservation.data.remote

import com.littlelemon.application.core.data.remote.SupabaseRPC
import com.littlelemon.application.core.data.remote.SupabaseTables
import com.littlelemon.application.reservation.data.remote.models.ReservationDTO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class SupabaseReservationRemoteDataSource(
    private val client: SupabaseClient
) : ReservationRemoteDataSource {

    private val selectionColumns =
        Columns.list("id, reservation_time, party_size, status, special_instructions")

    @Throws(
        PostgrestRestException::class,
        HttpRequestTimeoutException::class,
        HttpRequestException::class
    )
    override suspend fun getReservations(): List<ReservationDTO> {
        return client.from(SupabaseTables.RESERVATIONS)
            .select(columns = selectionColumns)
            .decodeList<ReservationDTO>()
    }


    @Throws(
        PostgrestRestException::class,
        HttpRequestTimeoutException::class,
        HttpRequestException::class
    )
    override suspend fun makeReservation(reservation: ReservationDTO): ReservationDTO {
        return client.from(SupabaseTables.RESERVATIONS)
            .insert(reservation) { select(selectionColumns) }.decodeSingle()
    }


    @Throws(
        PostgrestRestException::class,
        HttpRequestTimeoutException::class,
        HttpRequestException::class
    )
    override suspend fun cancelReservation(reservation: ReservationDTO): ReservationDTO {
        return client.postgrest.rpc(SupabaseRPC.MakeReservation.RPC_NAME) {
            buildJsonObject {
                put(SupabaseRPC.MakeReservation.P_TIME, reservation.reservationTime)
                put(SupabaseRPC.MakeReservation.P_SIZE, reservation.people)
                put(SupabaseRPC.MakeReservation.P_INSTRUCTIONS, reservation.specialInstructions)
            }
        }.decodeSingle()
    }
}