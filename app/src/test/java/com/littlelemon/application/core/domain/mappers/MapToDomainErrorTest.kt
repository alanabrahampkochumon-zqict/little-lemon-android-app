package com.littlelemon.application.core.domain.mappers

import com.littlelemon.application.core.domain.utils.Error
import com.littlelemon.application.utils.FakeKtorBackend.getErrorResponse
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.HttpRequestBuilder
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class MapToDomainErrorTest {

    @Test
    fun onConversion_fromPostgrestException_returnsNetworkErrorWithCorrectCode() = runTest {
        // Arrange
        val response = getErrorResponse()
        val exception = PostgrestRestException(
            message = "Bad Request",
            code = "400",
            hint = null,
            details = null,
            response = response
        )

        // Act
        val error = exception.mapToDomainError()

        // Assert
        assertTrue(error is Error.NetworkError.BadRequest)

    }

    @Test
    fun onConversion_fromHttpRequestTimeoutException_returnsNetworkErrorTimeout() {
        // Arrange
        val exception = HttpRequestTimeoutException(
            url = "something.com",
            timeoutMillis = 1000L
        )

        // Act
        val error = exception.mapToDomainError()

        // Assert
        assertTrue(error is Error.NetworkError.Timeout)
    }

    @Test
    fun onConversionO_fromHttpRequestException_returnsNetworkErrorUnknown() {
        // Arrange
        val exception = HttpRequestException(
            message = "error message",
            request = HttpRequestBuilder()
        )

        // Act
        val error = exception.mapToDomainError()

        // Assert
        assertTrue(error is Error.NetworkError.Unknown)
    }

    @Test
    fun onConversion_fromIllegalStateException_returnErrorDatabase() {
        // Arrange
        val exception = IllegalStateException()

        // Act
        val error = exception.mapToDomainError()

        // Assert
        assertTrue(error is Error.Database)
    }

    @Test
    fun onConversion_fromAnyOtherException_returnsErrorUnknown() {
        // Arrange
        val exception = Exception()

        // Act
        val error = exception.mapToDomainError()

        // Assert
        assertTrue(error is Error.Unknown)
    }

}