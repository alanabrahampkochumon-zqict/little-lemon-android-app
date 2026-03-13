package com.littlelemon.application.address.data.remote

import com.google.maps.errors.OverDailyLimitException
import com.google.maps.errors.OverQueryLimitException
import com.google.maps.model.GeocodingResult
import com.google.maps.model.Geometry
import com.google.maps.model.LatLng
import com.google.maps.model.LocationType
import com.littlelemon.application.address.data.remote.geocoding.GeocodingEngine
import com.littlelemon.application.address.data.remote.geocoding.GeocodingRemoteDataSource
import com.littlelemon.application.address.data.remote.geocoding.GoogleGeocodingRemoteDataSource
import com.littlelemon.application.address.domain.GeocoderException
import com.littlelemon.application.core.domain.exceptions.InvalidRequestException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GoogleGeocodingRemoteDataSourceTest {

    private val address = "Some Fake Address, Fake Street, Fake City, Fake State - 123456"
    private val latLng = LatLng(12.34, 12.123)
    private lateinit var geocodingEngine: GeocodingEngine

    private lateinit var dataSource: GeocodingRemoteDataSource

    private lateinit var geocodingResult: GeocodingResult

    @BeforeEach
    fun setUp() {
        geocodingEngine = mockk()
        dataSource = GoogleGeocodingRemoteDataSource(geocodingEngine)

        val geometry = Geometry()
        geometry.location = latLng
        geometry.locationType = LocationType.ROOFTOP

        geocodingResult = GeocodingResult()
        geocodingResult.formattedAddress = address
        geocodingResult.geometry = geometry
        geocodingResult.partialMatch = true
    }

    @Test
    fun geocoder_whenEngineReturnsNoResult_throwsNoResultException() = runTest {
        // Given the engine returns an empty list
        coEvery { geocodingEngine.geocode(any()) } returns emptyArray()

        // When address is geocoded
        // Then, it throws a `GeocoderException.ZeroResults`
        assertThrows<GeocoderException.ZeroResults> { dataSource.geocodeAddress(address) }
    }

    @Test
    fun geocoder_whenEngineThrowsIllegalArgumentException_throwsInvalidRequestException() =
        runTest {
            // Given the engine throws IllegalArgumentException
            coEvery { geocodingEngine.geocode(any()) } throws IllegalArgumentException()

            // When address is geocoded
            // Then, it throws a `InvalidRequestException`
            assertThrows<InvalidRequestException> { dataSource.geocodeAddress(address) }
        }

    @Test
    fun geocoder_whenEngineThrowsIllegalStateException_throwsInvalidRequestException() = runTest {
        // Given the engine throws IllegalStateException
        coEvery { geocodingEngine.geocode(any()) } throws IllegalStateException()

        // When address is geocoded
        // Then, it throws a `InvalidRequestException`
        assertThrows<InvalidRequestException> { dataSource.geocodeAddress(address) }
    }

    @Test
    fun geocoder_whenEngineThrowsOverDailyLimitException_throwsGeocoderDailyLimitException() =
        runTest {
            // Given the engine throws OverDailyLimitException
            coEvery { geocodingEngine.geocode(any()) } throws OverDailyLimitException(null)

            // When address is geocoded
            // Then, it throws a `GeocoderException.DailyLimit`
            assertThrows<GeocoderException.DailyLimit> { dataSource.geocodeAddress(address) }
        }

    @Test
    fun geocoder_whenEngineThrowsOverQueryLimitException_throwsGeocoderQueryLimitException() =
        runTest {
            // Given the engine throws OverDailyLimitException
            coEvery { geocodingEngine.geocode(any()) } throws OverQueryLimitException(null)

            // When address is geocoded
            // Then, it throws a `GeocoderException.QueryLimit`
            assertThrows<GeocoderException.QueryLimit> { dataSource.geocodeAddress(address) }
        }

    @Test
    fun geocoder_whenEngineReturnsResults_returnsGeocodingDTO() = runTest {
        // Given the engine returns geocoding result

        coEvery { geocodingEngine.geocode(any()) } returns arrayOf(geocodingResult)

        // When address is geocoded
        val geocodingDTO = dataSource.geocodeAddress(address)

        // Then, it returns correct result
//        assertEquals(geocodingResult.geometry.locationType)
    }

}