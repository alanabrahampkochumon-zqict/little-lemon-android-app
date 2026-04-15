package com.littlelemon.application.reservation.data.remote

import com.littlelemon.application.reservation.data.remote.models.ReservationDTO
import com.littlelemon.application.reservations.utils.ReservationGenerator
import com.littlelemon.application.utils.createFakeSupabaseClient
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class SupabaseReservationRemoteDataSourceTest {

    private lateinit var client: SupabaseClient
    private lateinit var remoteDataSource: SupabaseReservationRemoteDataSource

    private val errorResponse = """
        {
            "message": "Bad Request",
            "code": 400
        }
    """.trimIndent()
    private val errorCode = HttpStatusCode.BadRequest


    @Nested
    inner class GetReservationsTests {

        @Test
        fun remoteSuccess_returnsCorrectListOfReservations() = runTest {
            val expectedReservations = List(5) { ReservationGenerator.generateReservationDTO() }
            val response = Json.encodeToString(expectedReservations)
            client = createFakeSupabaseClient {
                respond(
                    response,
                    HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            remoteDataSource = SupabaseReservationRemoteDataSource(client)

            val reservations = remoteDataSource.getReservations()

            assertEquals(expectedReservations, reservations)

        }

        @Test
        fun noReservations_returnsEmptyList() = runTest {
            val expectedReservations = emptyList<ReservationDTO>()
            val response = Json.encodeToString(expectedReservations)
            client = createFakeSupabaseClient {
                respond(
                    response,
                    HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            remoteDataSource = SupabaseReservationRemoteDataSource(client)

            val reservations = remoteDataSource.getReservations()

            assertEquals(expectedReservations, reservations)
        }

        @Test
        fun remoteError_throwsException() = runTest {
            client = createFakeSupabaseClient {
                respond(
                    errorResponse,
                    errorCode,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            remoteDataSource = SupabaseReservationRemoteDataSource(client)

            assertThrows<PostgrestRestException> { remoteDataSource.getReservations() }
        }
    }

    @Nested
    inner class MakeReservationTests {

        @Test
        fun remoteSuccess_returnsNewReservation() = runTest {
            val expectedReservation = List(1) { ReservationGenerator.generateReservationDTO() }
            val response = Json.encodeToString(expectedReservation)
            client = createFakeSupabaseClient {
                respond(
                    response,
                    HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            remoteDataSource = SupabaseReservationRemoteDataSource(client)

            val reservation = remoteDataSource.makeReservation(expectedReservation.first())

            assertEquals(expectedReservation.first(), reservation)
        }


        @Test
        fun remoteError_throwsException() = runTest {
            val expectedReservation = ReservationGenerator.generateReservationDTO()
            client = createFakeSupabaseClient {
                respond(
                    errorResponse,
                    errorCode,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            remoteDataSource = SupabaseReservationRemoteDataSource(client)

            assertThrows<PostgrestRestException> {
                remoteDataSource.makeReservation(
                    expectedReservation
                )
            }
        }
    }
}