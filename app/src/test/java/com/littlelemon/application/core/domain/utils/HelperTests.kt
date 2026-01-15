package com.littlelemon.application.core.domain.utils

import io.ktor.http.HttpStatusCode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class HelperTests {

    @Nested
    inner class NetworkErrorConversionTests {

        @Test
        fun httpStatusSuccess_returnsNull() {
            // Arrange
            val status = HttpStatusCode.OK

            // Act
            val error = status.toNetworkError()

            // Assert
            assertNull(error)
        }

        @Test
        fun httpStatusMovedPermanently_returnsNull() {
            // Arrange
            val status = HttpStatusCode.MovedPermanently

            // Act
            val error = status.toNetworkError()

            // Assert
            assertNull(error)
        }

        @Test
        fun httpStatusNoContent_returnsNull() {
            // Arrange
            val status = HttpStatusCode.NoContent

            // Act
            val error = status.toNetworkError()

            // Assert
            assertNull(error)
        }

        @Test
        fun httpStatusNotFound_returnsNotFound() {
            // Arrange
            val status = HttpStatusCode.NotFound

            // Act
            val error = status.toNetworkError()

            // Assert
            assertTrue(error is Error.NetworkError.NotFound)
        }

        @Test
        fun httpStatusUnauthorized_returnsUnauthorized() {
            // Arrange
            val status = HttpStatusCode.Unauthorized

            // Act
            val error = status.toNetworkError()

            // Assert
            assertTrue(error is Error.NetworkError.Unauthorized)
        }

        @Test
        fun httpStatusForbidden_returnsForbidden() {
            // Arrange
            val status = HttpStatusCode.Forbidden

            // Act
            val error = status.toNetworkError()

            // Assert
            assertTrue(error is Error.NetworkError.Forbidden)
        }

        @Test
        fun httpStatusBadRequest_returnsBadRequest() {
            // Arrange
            val status = HttpStatusCode.BadRequest

            // Act
            val error = status.toNetworkError()

            // Assert
            assertTrue(error is Error.NetworkError.BadRequest)
        }

        @Test
        fun httpStatusServerError_returnsUnknownError() {
            // Arrange
            val status = HttpStatusCode.InternalServerError

            // Act
            val error = status.toNetworkError()

            // Assert
            assertTrue(error is Error.NetworkError.Unknown)
        }
    }

    @Nested
    inner class NetworkErrorCodeConversionTests {

        @Test
        fun httpStatusSuccess_returnsNull() {
            // Arrange
            val statusCode = HttpStatusCode.OK.value

            // Act
            val error = statusCode.toNetworkError()

            // Assert
            assertNull(error)
        }

        @Test
        fun httpStatusMovedPermanently_returnsNull() {
            // Arrange
            val statusCode = HttpStatusCode.MovedPermanently.value

            // Act
            val error = statusCode.toNetworkError()

            // Assert
            assertNull(error)
        }

        @Test
        fun httpStatusNoContent_returnsNull() {
            // Arrange
            val statusCode = HttpStatusCode.NoContent.value

            // Act
            val error = statusCode.toNetworkError()

            // Assert
            assertNull(error)
        }

        @Test
        fun httpStatusNotFound_returnsNotFound() {
            // Arrange
            val statusCode = HttpStatusCode.NotFound.value

            // Act
            val error = statusCode.toNetworkError()

            // Assert
            assertTrue(error is Error.NetworkError.NotFound)
        }

        @Test
        fun httpStatusUnauthorized_returnsUnauthorized() {
            // Arrange
            val statusCode = HttpStatusCode.Unauthorized.value

            // Act
            val error = statusCode.toNetworkError()

            // Assert
            assertTrue(error is Error.NetworkError.Unauthorized)
        }

        @Test
        fun httpStatusForbidden_returnsForbidden() {
            // Arrange
            val statusCode = HttpStatusCode.Forbidden.value

            // Act
            val error = statusCode.toNetworkError()

            // Assert
            assertTrue(error is Error.NetworkError.Forbidden)
        }

        @Test
        fun httpStatusBadRequest_returnsBadRequest() {
            // Arrange
            val statusCode = HttpStatusCode.BadRequest.value

            // Act
            val error = statusCode.toNetworkError()

            // Assert
            assertTrue(error is Error.NetworkError.BadRequest)
        }

        @Test
        fun httpStatusServerError_returnsUnknownError() {
            // Arrange
            val statusCode=  HttpStatusCode.InternalServerError.value

            // Act
            val error = statusCode.toNetworkError()

            // Assert
            assertTrue(error is Error.NetworkError.Unknown)
        }
    }

}