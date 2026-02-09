package com.littlelemon.application.address.data.remote

import com.littlelemon.application.address.data.mappers.toRequestDTO
import com.littlelemon.application.utils.AddressGenerator
import com.littlelemon.application.utils.createFakeSupabaseClient
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class AddressRemoteDataSourceImplTest {

    private lateinit var client: SupabaseClient
    private lateinit var remoteDatasource: AddressRemoteDataSource

    private val errorResponse = """
        {
            "message": "Bad Request",
            "code": 400
        }
    """.trimIndent()

    @Test
    fun onGetAddress_remoteSuccess_returnListOfAddresses() = runTest {
        // Arrange
        val numAddress = 5
        val addresses = List(numAddress) {
            AddressGenerator.generateAddressDTO()
        }
        val response = Json.encodeToString(addresses)
        client = createFakeSupabaseClient {
            respond(
                response,
                HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        remoteDatasource = AddressRemoteDataSourceImpl(client)

        // Act
        val result = remoteDatasource.getAddress()

        // Assert
        assertEquals(addresses, result)
    }

    @Test
    fun onGetAddress_remoteFailure_throwsException() = runTest {

        // Arrange
        client = createFakeSupabaseClient {
            respond(
                errorResponse,
                HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        remoteDatasource = AddressRemoteDataSourceImpl(client)

        // Act & Assert
        assertThrows<PostgrestRestException> { remoteDatasource.getAddress() }
    }

    @Test
    fun onSaveAddress_remoteSuccess_returnsSavedAddress() = runTest {
        // Arrange
        val address = AddressGenerator.generateAddressDTO()
        val request = Json.encodeToString(listOf(address.toRequestDTO()))
        client = createFakeSupabaseClient {
            respond(
                request,
                HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        remoteDatasource = AddressRemoteDataSourceImpl(client)

        // Act
        val response = remoteDatasource.saveAddress(address.toRequestDTO())

        // Assert
        assertEquals(address, response)
    }

    @Test
    fun onSaveAddress_remoteFailure_throwsException() = runTest {
        // Arrange
        val address = AddressGenerator.generateAddressDTO()
        client = createFakeSupabaseClient {
            respond(
                errorResponse,
                HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        remoteDatasource = AddressRemoteDataSourceImpl(client)

        // Act & Assert
        assertThrows<PostgrestRestException> { remoteDatasource.saveAddress(address.toRequestDTO()) }
    }

    @Test
    fun onUpdateAddress_remoteSuccess_returnsUpdatedAddress() = runTest {
        // Arrange
        val address = AddressGenerator.generateAddressDTO()
        val request = Json.encodeToString(listOf(address.toRequestDTO()))
        client = createFakeSupabaseClient {
            respond(
                request,
                HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        remoteDatasource = AddressRemoteDataSourceImpl(client)

        // Act
        val response = remoteDatasource.updateAddress(address.toRequestDTO())

        // Assert
        assertEquals(address, response)
    }

    @Test
    fun onUpdateAddress_remoteFailure_throwsException() = runTest {
        // Arrange
        val address = AddressGenerator.generateAddressDTO()
        client = createFakeSupabaseClient {
            respond(
                errorResponse,
                HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        remoteDatasource = AddressRemoteDataSourceImpl(client)

        // Act & Assert
        assertThrows<PostgrestRestException> { remoteDatasource.updateAddress(address.toRequestDTO()) }
    }

    @Test
    fun onDeleteAddress_remoteSuccess_returnsTrue() = runTest {
        val address = AddressGenerator.generateAddressDTO()
        val request = Json.encodeToString(listOf(address.toRequestDTO()))
        client = createFakeSupabaseClient {
            respond(
                request,
                HttpStatusCode.NoContent,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        remoteDatasource = AddressRemoteDataSourceImpl(client)

        // Act & Assert
        assertDoesNotThrow { remoteDatasource.deleteAddress(address.toRequestDTO()) }
    }

    @Test
    fun onDeleteAddress_remoteFailure_throwsException() = runTest {
        // Arrange
        val address = AddressGenerator.generateAddressDTO()
        client = createFakeSupabaseClient {
            respond(
                errorResponse,
                HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        remoteDatasource = AddressRemoteDataSourceImpl(client)

        // Act & Assert
        assertThrows<PostgrestRestException> { remoteDatasource.deleteAddress(address.toRequestDTO()) }
    }
}