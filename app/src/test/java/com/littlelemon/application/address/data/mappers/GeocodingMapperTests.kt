package com.littlelemon.application.address.data.mappers

import com.google.maps.errors.ApiException
import com.google.maps.errors.OverDailyLimitException
import com.google.maps.errors.OverQueryLimitException
import com.google.maps.errors.ZeroResultsException
import com.google.maps.model.AddressComponentType
import com.google.maps.model.GeocodingResult
import com.google.maps.model.Geometry
import com.google.maps.model.LatLng
import com.google.maps.model.LocationType
import com.littlelemon.application.address.data.remote.models.GeocodingDTO
import com.littlelemon.application.address.domain.GeocoderException
import com.littlelemon.application.address.utils.GeocodingGenerator
import com.littlelemon.application.core.domain.exceptions.CoreException
import com.littlelemon.application.core.domain.exceptions.InvalidRequestException
import com.littlelemon.application.core.domain.exceptions.RequestDeniedException
import com.littlelemon.application.core.domain.exceptions.UnknownException
import org.junit.jupiter.api.Nested
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
                LocationType.RANGE_INTERPOLATED, GeocodingDTO.LocationType.RANGE_INTERPOLATED
            ),
            Arguments.of(LocationType.APPROXIMATE, GeocodingDTO.LocationType.APPROXIMATE),
            Arguments.of(LocationType.UNKNOWN, GeocodingDTO.LocationType.UNKNOWN),
        )

        @JvmStatic
        fun addressComponentProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(AddressComponentType.STREET_ADDRESS),
            Arguments.of(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1),
            Arguments.of(AddressComponentType.LOCALITY),
            Arguments.of(AddressComponentType.COUNTRY),
            Arguments.of(AddressComponentType.POSTAL_CODE),
            null
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
        from: LocationType, to: GeocodingDTO.LocationType
    ) {
        // When mapping from Google's LocationType to GeocodingDTO.LocationType
        val result = from.toLocationTypeDTO()

        // Then, the mapping is as expected
        assertEquals(to, result)
    }


    @Nested
    inner class GeocodingResultMapper {

        @ParameterizedTest
        @MethodSource("com.littlelemon.application.address.data.mappers.GeocodingMapperTests#addressComponentProvider")
        fun whenMappingGeocodingResultWithMissingAddressComponents_returnsEmptyString(
            missingAddressComponent: AddressComponentType?
        ) {
            // Given a geocoding result with an address component missing
            val (geocodingResult, expectedDto) = GeocodingGenerator.generateGeocodingResult(
                missingAddressComponent
            )

            // When mapped to address dto
            val actualDTO = geocodingResult.toGeocodingDTO()

            // Then it returns correct dto
            assertEquals(expectedDto, actualDTO)
        }

        @Test
        fun whenMappingGeocodingResultWithEmptyFullAddress_returnsDTOWithBlankFullAddress() {
            // Given a geocoding result with null address
            val (geocodingResult, expectedDTO) = GeocodingGenerator.generateGeocodingResult(
                makeFullAddressEmpty = true
            )

            // When mapped to address DTO
            val actualDTO = geocodingResult.toGeocodingDTO()

            // Then it returns correct DTO
            assertEquals(
                expectedDTO.copy(
                    address = expectedDTO.address?.copy(address = ""), fullAddress = ""
                ), actualDTO
            )

        }

        @Test
        fun whenMappingGeocodingResultNullFullAddress_returnsDTOWithBlankFullAddress() {
            // Given a geocoding result with null address
            val (geocodingResult, expectedDTO) = GeocodingGenerator.generateGeocodingResult()
            geocodingResult.formattedAddress = null

            // When mapped to address DTO
            val actualDTO = geocodingResult.toGeocodingDTO()

            // Then it returns correct DTO
            assertEquals(
                expectedDTO.copy(fullAddress = "", address = actualDTO.address?.copy(address = "")),
                actualDTO
            )
        }

        @Test
        fun whenMappingGeocodingResultNullPlaceID_returnsDTOWithBlankPlaceID() {
            // Given a geocoding result with null address
            val (geocodingResult, expectedDTO) = GeocodingGenerator.generateGeocodingResult()
            geocodingResult.placeId = null

            // When mapped to address DTO
            val actualDTO = geocodingResult.toGeocodingDTO()

            // Then it returns correct DTO
            assertEquals(expectedDTO.copy(placeId = ""), actualDTO)
        }

        @Test
        fun whenMappingGeocodingResultNullAddressComponent_returnsDTOWithEmptyAddressFieldsExceptAddress() {
            // Given a geocoding result with null address
            val (geocodingResult, expectedDTO) = GeocodingGenerator.generateGeocodingResult()
            geocodingResult.addressComponents = null

            // When mapped to address DTO
            val actualDTO = geocodingResult.toGeocodingDTO()

            // Then it returns correct DTO
            assertEquals(
                expectedDTO.copy(
                    address = expectedDTO.address?.copy(
                        streetAddress = "", city = "", state = "", country = "", pinCode = ""
                    )
                ), actualDTO
            )
        }

    }

    @Test
    fun geocodingResultMapper_mapsToCorrectDTO() {
        // Given a geocoding result
        val geometry = Geometry()
        geometry.location = LatLng(1.234, 5.4213)
        geometry.locationType = LocationType.ROOFTOP

        val geocodingResult = GeocodingResult()
        geocodingResult.geometry = geometry
        geocodingResult.formattedAddress = "Address, Street, City, Country, Pincode"
        geocodingResult.partialMatch = true

        // When mapped to GeocodingDTO
        val dto = geocodingResult.toGeocodingDTO()

        // Then, it has the correct result
        assertEquals(geometry.location.lat, dto.latLng.lat)
        assertEquals(geometry.location.lng, dto.latLng.lng)
        assertEquals(geometry.locationType.toLocationTypeDTO(), dto.locationType)
        assertEquals(geocodingResult.formattedAddress, dto.fullAddress)
        assertEquals(geocodingResult.partialMatch, dto.partialMatch)
    }

    @Nested
    inner class GeocodingDTOToEntityMapperTests() {

        @Test
        fun whenMapping_withNullAddress_returnsEntityWithNullAddress() {
            // When mapping a DTO with null address
            val (entity, _, dto) = GeocodingGenerator.generateGeocodingEntities()
            val actualAddress = dto.copy(address = null).toGeocodingEntity()

            // Then, a geocoding entity with null address is returned
            // NOTE: Timestamp removed to test flakiness
            assertEquals(
                entity.copy(address = null, createdTimestamp = 0L),
                actualAddress.copy(createdTimestamp = 0L)
            )
        }

        @Test
        fun whenMapping_withEntireNonNullFields_returnsFullGeocodedAddress() {
            // When mapping an DTO
            val (entity, _, dto) = GeocodingGenerator.generateGeocodingEntities()
            val actualAddress = dto.toGeocodingEntity()

            // Then, a full matching geocoded address is returned
            // NOTE: Timestamp removed to test flakiness
            assertEquals(
                entity.copy(createdTimestamp = 0L),
                actualAddress.copy(createdTimestamp = 0L)
            )

        }
    }

    @Nested
    inner class GeocodingEntityToGeocodedAddressMapperTests() {

        @Test
        fun whenMapping_withNullAddress_returnsGeocodedAddressWithNullAddress() {
            // When mapping an Entity with null address
            val (entity, local, _) = GeocodingGenerator.generateGeocodingEntities()
            val actualAddress = entity.copy(address = null).toGeocodedAddress()

            // Then, a geocoded address with null address is returned
            assertEquals(local.copy(address = null), actualAddress)
        }

        @Test
        fun whenMapping_withEntireNonNullFields_returnsFullGeocodedAddress() {
            // When mapping an Entity
            val (entity, local, _) = GeocodingGenerator.generateGeocodingEntities()
            val actualAddress = entity.toGeocodedAddress()

            // Then, a full matching geocoded address is returned
            assertEquals(local, actualAddress)

        }
    }


}