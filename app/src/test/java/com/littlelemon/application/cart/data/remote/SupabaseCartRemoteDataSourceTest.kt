package com.littlelemon.application.cart.data.remote

import com.littlelemon.application.cart.data.remote.models.CartItemDTO
import com.littlelemon.application.utils.createFakeSupabaseClient
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class SupabaseCartRemoteDataSourceTest {

    private lateinit var client: SupabaseClient
    private lateinit var dataSource: CartRemoteDataSource

    private val errorResponse = """
        {
            "message": "Bad Request",
            "code": 400
        }
    """.trimIndent()
    private val errorCode = HttpStatusCode.BadRequest

    private val cartItem = CartItemDTO("1234", 5)

    @BeforeEach
    fun setUp() {

    }

    @Nested
    inner class AddToCart {

        @Test
        fun networkError_throwsError() = runTest {
            client = createFakeSupabaseClient {
                respond(
                    errorResponse,
                    errorCode,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            dataSource = SupabaseCartRemoteDataSource(client)

            assertThrows<PostgrestRestException> { dataSource.addToCart(cartItem) }
        }

        @Test
        fun networkSuccess_throwsNoError() = runTest {
            val response = Json.encodeToString(cartItem)
            client = createFakeSupabaseClient {
                respond(
                    response,
                    HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            dataSource = SupabaseCartRemoteDataSource(client)

            assertDoesNotThrow { dataSource.addToCart(cartItem) }
        }
    }


}