package com.littlelemon.application.cart.data.remote

import com.littlelemon.application.cart.data.remote.models.CartItemDTO
import com.littlelemon.application.cart.data.remote.models.CartSummaryDTO
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
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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

    @OptIn(ExperimentalUuidApi::class)
    private val cartItems =
        List(5) { CartItemDTO(Uuid.generateV4().toString(), Random.nextInt(5, 10)) }

    @BeforeEach
    fun setUp() {

    }

    @Nested
    inner class UpdateCart {

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

            assertThrows<PostgrestRestException> { dataSource.updateCart(cartItem) }
        }

        @Test
        fun networkSuccess_returnsUpdatedDTO() = runTest {
            val response = Json.encodeToString(listOf(cartItem))
            client = createFakeSupabaseClient {
                respond(
                    response,
                    HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            dataSource = SupabaseCartRemoteDataSource(client)

            val dto = dataSource.updateCart(cartItem)
            assertEquals(cartItem, dto)
        }
    }


    @Nested
    inner class ClearCart {

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

            assertThrows<PostgrestRestException> { dataSource.clearCart() }
        }

        @Test
        fun networkSuccess_returnsUpdatedDTO() = runTest {
            val response = Json.encodeToString(listOf(cartItem))
            client = createFakeSupabaseClient {
                respond(
                    response,
                    HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            dataSource = SupabaseCartRemoteDataSource(client)

            assertDoesNotThrow { dataSource.clearCart() }
        }
    }


    @Nested
    inner class GetCart {

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

            assertThrows<PostgrestRestException> { dataSource.getCart() }
        }

        @Test
        fun networkSuccess_returnsUpdatedDTO() = runTest {
            val response = Json.encodeToString(cartItems)
            client = createFakeSupabaseClient {
                respond(
                    response,
                    HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            dataSource = SupabaseCartRemoteDataSource(client)

            val retrievedCartItems = dataSource.getCart()

            assertEquals(cartItems, retrievedCartItems)
        }
    }


    @Nested
    inner class GetCartSummary {

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

            assertThrows<PostgrestRestException> { dataSource.getCartSummary() }
        }

        @Test
        fun networkSuccess_returnsUpdatedDTO() = runTest {
            val cartSummary = CartSummaryDTO(150, 10, 5, 0, 155)
            val response = Json.encodeToString(listOf(cartSummary))
            client = createFakeSupabaseClient {
                respond(
                    response,
                    HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            dataSource = SupabaseCartRemoteDataSource(client)

            val retrievedCartSummary = dataSource.getCartSummary()

            assertEquals(cartSummary, retrievedCartSummary)
        }
    }


}