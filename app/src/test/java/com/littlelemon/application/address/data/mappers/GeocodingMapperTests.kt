package com.littlelemon.application.address.data.mappers

import com.google.maps.errors.ApiException
import com.google.maps.errors.OverDailyLimitException
import com.google.maps.errors.OverQueryLimitException
import com.google.maps.errors.ZeroResultsException
import com.google.maps.model.GeocodingResult
import com.google.maps.model.Geometry
import com.google.maps.model.LatLng
import com.google.maps.model.LocationType
import com.littlelemon.application.address.data.remote.models.GeocodingDTO
import com.littlelemon.application.address.domain.GeocoderException
import com.littlelemon.application.core.domain.exceptions.CoreException
import com.littlelemon.application.core.domain.exceptions.InvalidRequestException
import com.littlelemon.application.core.domain.exceptions.RequestDeniedException
import com.littlelemon.application.core.domain.exceptions.UnknownException
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals
import com.google.maps.errors.InvalidRequestException as GeoApiInvalidRequestException
import com.google.maps.errors.RequestDeniedException as GeoApiRequestDeniedException

class GeocodingMapperTests {

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

        @JvmStatic
        fun locationTypeProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(LocationType.ROOFTOP, GeocodingDTO.LocationType.ROOFTOP),
            Arguments.of(LocationType.GEOMETRIC_CENTER, GeocodingDTO.LocationType.GEOMETRIC_CENTER),
            Arguments.of(
                LocationType.RANGE_INTERPOLATED,
                GeocodingDTO.LocationType.RANGE_INTERPOLATED
            ),
            Arguments.of(LocationType.APPROXIMATE, GeocodingDTO.LocationType.APPROXIMATE),
            Arguments.of(LocationType.UNKNOWN, GeocodingDTO.LocationType.UNKNOWN),
        )
    }

    @ParameterizedTest
    @MethodSource("exceptionProvider")
    fun exceptionMapper_mapsToCorrectException(from: ApiException, to: CoreException) {
        // When mapping from Geocoding exceptions to CoreExceptions
        val result = from.toGeocodingException()

        // Then, the exception is of expected type
        assertEquals(result::class, to::class)
    }

    @ParameterizedTest
    @MethodSource("locationTypeProvider")
    fun locationTypeMapper_mapsToCorrectDTOLocationType(
        from: LocationType,
        to: GeocodingDTO.LocationType
    ) {
        // When mapping from Google's LocationType to GeocodingDTO.LocationType
        val result = from.toLocationTypeDTO()

        // Then, the mapping is as expected
        assertEquals(to, result)
    }

    @Test
    fun geocodingResultMapper_mapsToCorrectDTO() {
        // Given a geocoding result
        val geometry = Geometry()
        geometry.location = LatLng(1.234, 5.4213)
        geometry.locationType = LocationType.ROOFTOP

        val geocodingResult = GeocodingResult()
        geocodingResult.geometry = geometry
        geocodingResult.partialMatch = true

        // When mapped to GeocodingDTO
        val dto = geocodingResult.toGeocodingDTO()

        // Then, it has the correct result
        assertEquals(geometry.location.lat, dto.latLng.lat)
        assertEquals(geometry.location.lng, dto.latLng.lng)
        assertEquals(geometry.locationType.toLocationTypeDTO(), dto.locationType)
        assertEquals(geocodingResult.partialMatch, dto.partialMatch)
    }
}