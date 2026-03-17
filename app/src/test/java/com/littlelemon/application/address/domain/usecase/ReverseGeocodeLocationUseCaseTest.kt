package com.littlelemon.application.address.domain.usecase

import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.models.GeocodedAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.address.utils.GeocodingGenerator
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ReverseGeocodeLocationUseCaseTest {

    private lateinit var repository: AddressRepository
    private lateinit var useCase: ReverseGeocodeLocationUseCase


    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = ReverseGeocodeLocationUseCase(repository)
    }

    @Test
    fun reverseGeocodeLocation_repositorySuccess_returnsSuccessResult() = runTest {
        // Given repository success
        val (_, local, _) = GeocodingGenerator.generateGeocodingEntities()
        coEvery { repository.reverseGeocodeLocation(any()) } returns Resource.Success(local)

        // When usecase is invoke
        val geocodedAddress = useCase(local.location!!)

        // Then success is returned
        assertIs<Resource.Success<GeocodedAddress>>(geocodedAddress)
        // And the response contains data
        assertEquals(local, geocodedAddress.data)
    }

    @Test
    fun reverseGeocodeLocation_repositoryFailure_returnsFailureResult() = runTest {
        // Given repository failure
        coEvery { repository.reverseGeocodeLocation(any()) } returns Resource.Failure()

        // When usecase is invoke
        val geocodedAddress = useCase(LocalLocation(1.234, 2.3435))

        // Then failure is returned
        assertIs<Resource.Failure<GeocodedAddress>>(geocodedAddress)
        // And data is null
        assertNull(geocodedAddress.data)
    }
}