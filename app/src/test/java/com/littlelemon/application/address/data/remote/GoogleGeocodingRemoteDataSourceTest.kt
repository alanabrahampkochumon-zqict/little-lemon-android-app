package com.littlelemon.application.address.data.remote

import com.google.maps.errors.OverDailyLimitException
import com.google.maps.errors.OverQueryLimitException
import com.google.maps.model.GeocodingResult
import com.littlelemon.application.address.data.remote.geocoding.GeocodingEngine
import com.littlelemon.application.address.data.remote.geocoding.GeocodingRemoteDataSource
import com.littlelemon.application.address.data.remote.geocoding.GoogleGeocodingRemoteDataSource
import com.littlelemon.application.address.data.remote.models.GeocodingDTO
import com.littlelemon.application.address.domain.GeocoderException
import com.littlelemon.application.address.utils.GeocodingResultGenerator
import com.littlelemon.application.core.domain.exceptions.InvalidRequestException
import com.littlelemon.application.core.domain.exceptions.RequestDeniedException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class GoogleGeocodingRemoteDataSourceTest {

    private lateinit var address: String
    private lateinit var latLngDto: GeocodingDTO.LatLng

    private lateinit var geocodingEngine: GeocodingEngine

    private lateinit var dataSource: GeocodingRemoteDataSource

    private lateinit var geocodingResult: GeocodingResult

    private lateinit var expectedDTO: GeocodingDTO

    @BeforeEach
    fun setUp() {
        geocodingEngine = mockk()
        dataSource = GoogleGeocodingRemoteDataSource(geocodingEngine)

        val (gResult, dto) = GeocodingResultGenerator.generateResult()

        latLngDto = GeocodingDTO.LatLng(
            gResult.geometry.location.lat,
            gResult.geometry.location.lng
        )
        address = gResult.formattedAddress
        geocodingResult = gResult
        expectedDTO = dto
    }

    @Nested
    inner class GeocodingTests {
        @Test
        fun whenEngineReturnsNoResult_throwsNoResultException() = runTest {
            // Given the engine returns an empty list
            coEvery { geocodingEngine.geocode(any()) } returns emptyArray()

            // When address is geocoded
            // Then, it throws a `GeocoderException.ZeroResults`
            assertThrows<GeocoderException.ZeroResults> { dataSource.geocodeAddress(address) }
        }

        @Test
        fun whenEngineThrowsIllegalArgumentException_throwsInvalidRequestException() =
            runTest {
                // Given the engine throws IllegalArgumentException
                coEvery { geocodingEngine.geocode(any()) } throws IllegalArgumentException()

                // When address is geocoded
                // Then, it throws a `InvalidRequestException`
                assertThrows<InvalidRequestException> { dataSource.geocodeAddress(address) }
            }

        @Test
        fun whenEngineThrowsIllegalStateException_throwsRequestDeniedException() =
            runTest {
                // Given the engine throws IllegalStateException
                coEvery { geocodingEngine.geocode(any()) } throws IllegalStateException()

                // When address is geocoded
                // Then, it throws a `RequestDeniedException`
                assertThrows<RequestDeniedException> { dataSource.geocodeAddress(address) }
            }

        @Test
        fun whenEngineThrowsOverDailyLimitException_throwsGeocoderDailyLimitException() =
            runTest {
                // Given the engine throws OverDailyLimitException
                coEvery { geocodingEngine.geocode(any()) } throws OverDailyLimitException(null)

                // When address is geocoded
                // Then, it throws a `GeocoderException.DailyLimit`
                assertThrows<GeocoderException.DailyLimit> { dataSource.geocodeAddress(address) }
            }

        @Test
        fun whenEngineThrowsOverQueryLimitException_throwsGeocoderQueryLimitException() =
            runTest {
                // Given the engine throws OverDailyLimitException
                coEvery { geocodingEngine.geocode(any()) } throws OverQueryLimitException(null)

                // When address is geocoded
                // Then, it throws a `GeocoderException.QueryLimit`
                assertThrows<GeocoderException.QueryLimit> { dataSource.geocodeAddress(address) }
            }

        @Test
        fun whenEngineReturnsResults_returnsGeocodingDTO() = runTest {
            // Given the engine returns geocoding result

            coEvery { geocodingEngine.geocode(any()) } returns arrayOf(geocodingResult)

            // When address is geocoded
            val geocodingDTO = dataSource.geocodeAddress(address)

            // Then, it returns correct result
            assertEquals(expectedDTO, geocodingDTO)
        }
    }

    @Nested
    inner class ReverseGeocodingTests {
        @Test
        fun whenEngineReturnsNoResult_throwsNoResultException() = runTest {
            // Given the engine returns an empty list
            coEvery { geocodingEngine.reverseGeocode(any()) } returns emptyArray()

            // When address is reverse geocoded
            // Then, it throws a `GeocoderException.ZeroResults`
            assertThrows<GeocoderException.ZeroResults> { dataSource.reverseGeocodeAddress(latLngDto) }
        }

        @Test
        fun whenEngineThrowsIllegalArgumentException_throwsInvalidRequestException() =
            runTest {
                // Given the engine throws IllegalArgumentException
                coEvery { geocodingEngine.reverseGeocode(any()) } throws IllegalArgumentException()

                // When address is reverse geocoded
                // Then, it throws a `InvalidRequestException`
                assertThrows<InvalidRequestException> { dataSource.reverseGeocodeAddress(latLngDto) }
            }

        @Test
        fun whenEngineThrowsIllegalStateException_throwsRequestDeniedException() =
            runTest {
                // Given the engine throws IllegalStateException
                coEvery { geocodingEngine.reverseGeocode(any()) } throws IllegalStateException()

                // When address is reverse geocoded
                // Then, it throws a `RequestDeniedException`
                assertThrows<RequestDeniedException> { dataSource.reverseGeocodeAddress(latLngDto) }
            }

        @Test
        fun whenEngineThrowsOverDailyLimitException_throwsGeocoderDailyLimitException() =
            runTest {
                // Given the engine throws OverDailyLimitException
                coEvery { geocodingEngine.reverseGeocode(any()) } throws OverDailyLimitException(
                    null
                )

                // When address is geocoded
                // Then, it throws a `GeocoderException.DailyLimit`
                assertThrows<GeocoderException.DailyLimit> {
                    dataSource.reverseGeocodeAddress(
                        latLngDto
                    )
                }
            }

        @Test
        fun whenEngineThrowsOverQueryLimitException_throwsGeocoderQueryLimitException() =
            runTest {
                // Given the engine throws OverDailyLimitException
                coEvery { geocodingEngine.reverseGeocode(any()) } throws OverQueryLimitException(
                    null
                )

                // When address is reverse geocoded
                // Then, it throws a `GeocoderException.QueryLimit`
                assertThrows<GeocoderException.QueryLimit> {
                    dataSource.reverseGeocodeAddress(
                        latLngDto
                    )
                }
            }

        @Test
        fun whenEngineReturnsResults_returnsGeocodingDTO() = runTest {
            // Given the engine returns geocoding result
            coEvery { geocodingEngine.reverseGeocode(any()) } returns arrayOf(geocodingResult)

            // When address is reverse geocoded
            val geocodingDTO = dataSource.reverseGeocodeAddress(latLngDto)

            // Then, it returns correct result
            assertEquals(expectedDTO, geocodingDTO)
        }
    }

}