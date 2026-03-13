package com.littlelemon.application.address.data.mappers

import com.google.maps.errors.ApiException
import com.google.maps.errors.OverDailyLimitException
import com.google.maps.errors.OverQueryLimitException
import com.google.maps.errors.ZeroResultsException
import com.littlelemon.application.address.domain.GeocoderException
import com.littlelemon.application.core.domain.exceptions.CoreException
import com.littlelemon.application.core.domain.exceptions.InvalidRequestException
import com.littlelemon.application.core.domain.exceptions.RequestDeniedException
import com.littlelemon.application.core.domain.exceptions.UnknownException
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals
import com.google.maps.errors.InvalidRequestException as GeoApiInvalidRequestException
import com.google.maps.errors.RequestDeniedException as GeoApiRequestDeniedException

class ToGeocodingExceptionTest {


    companion object {
        @JvmStatic
        fun exceptionProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(OverDailyLimitException(null), GeocoderException.DailyLimit()),
            Arguments.of(OverQueryLimitException(null), GeocoderException.QueryLimit()),
            Arguments.of(ZeroResultsException(null), GeocoderException.ZeroResults()),
            Arguments.of(GeoApiInvalidRequestException(null), InvalidRequestException()),
            Arguments.of(GeoApiRequestDeniedException(null), RequestDeniedException()),
            Arguments.of(ApiException.from("", ""), UnknownException()),
        )
    }

    @ParameterizedTest
    @MethodSource("exceptionProvider")
    fun exceptionMapper_mapsToCorrectException(from: ApiException, to: CoreException) {
        // When mapped from Geocoding exceptions to CoreExceptions
        val result = from.toGeocodingException()

        // Then, the exception is of expected type
        assertEquals(result::class, to::class)
    }
}