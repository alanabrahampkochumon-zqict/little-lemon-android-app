package com.littlelemon.application.core.domain.utils

import com.google.maps.errors.ApiException
import com.littlelemon.application.core.domain.exceptions.InvalidRequestException
import com.littlelemon.application.core.domain.exceptions.RequestDeniedException
import com.littlelemon.application.core.domain.exceptions.UnknownException
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SafeApiCallTest {


    companion object {
        @JvmStatic
        fun exceptionBlockProvider(): Stream<Arguments> = Stream.of(
            Arguments.of({ throw ApiException.from("", "") }, UnknownException::class),
            Arguments.of({ throw IllegalStateException() }, RequestDeniedException::class),
            Arguments.of({ throw IllegalArgumentException() }, InvalidRequestException::class),
            Arguments.of({ throw Exception() }, UnknownException::class),
        )
    }

    @ParameterizedTest
    @MethodSource("exceptionBlockProvider")
    fun safeAPICall_mapsToCorrectException(
        block: () -> Unit,
        expectedExceptionClass: KClass<out Throwable>
    ) = runTest {
        // When an exception throwing block is called
        val thrownException = assertThrows<Throwable> { safeApiCall { block() } }
        
        // Then, the exception is mapped correctly
        assertTrue(thrownException.instanceOf(expectedExceptionClass))
    }

    @Test
    fun safeAPICall_returnsBlockExecutionValue() = runTest {
        // Given a block that returns 5
        val block: () -> Int = block@{
            return@block 5
        }

        // When called using safeAPICall
        val result = safeApiCall { block() }

        // Then the result is returned
        assertEquals(5, result)
    }


}