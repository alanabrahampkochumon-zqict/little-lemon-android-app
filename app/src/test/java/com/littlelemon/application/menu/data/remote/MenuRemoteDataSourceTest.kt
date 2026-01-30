package com.littlelemon.application.menu.data.remote

import com.littlelemon.application.menu.utils.MenuDTOGenerator
import com.littlelemon.application.utils.createFakeSupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MenuRemoteDataSourceTest {

    @Test
    fun fetchDishes_remoteSuccessWithCorrectJson_returnsCorrectDTOObject() = runTest {
        // Arrange
        val generator = MenuDTOGenerator()
        val dishes = List(5) { generator.generateDishDTO() }
        val responseData = Json.encodeToString(dishes)
        val client = createFakeSupabaseClient(requestHandler = { request ->
            respond(
                content = responseData,
                status = HttpStatusCode.OK,
                headersOf(HttpHeaders.ContentType, "application/json")
            )
        })
        val dataSource = MenuRemoteDataSourceImpl(client)

        // Act
        val result = dataSource.fetchDishes()

        // Assert
        assertEquals(dishes.size, result.size)
        assertTrue(result.mapIndexed { index, dish -> dish == dishes[index] }
            .all { matching -> matching })
    }

    @Test
    fun fetchDishes_remoteSuccessWithEmptyBody_returnsEmptyList() = runTest {

        // Arrange
        val client = createFakeSupabaseClient(requestHandler = {

            respond(
                "[]", status = HttpStatusCode.OK,
                headersOf(HttpHeaders.ContentType, "application/json")
            )
        })
        val dataSource = MenuRemoteDataSourceImpl(client)

        // Act
        val result = dataSource.fetchDishes()

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun fetchDishes_remoteError_returnsEmptyList() = runTest {

        // Arrange
        val client = createFakeSupabaseClient(requestHandler = {
            respond(
                """{"message": "Not Found", "code": "404"}""", status = HttpStatusCode.BadRequest,
                headersOf(HttpHeaders.ContentType, "application/json")
            )
        })
        val dataSource = MenuRemoteDataSourceImpl(client)

        // Act & Assert
        assertThrows<RestException> { dataSource.fetchDishes() }
    }
}