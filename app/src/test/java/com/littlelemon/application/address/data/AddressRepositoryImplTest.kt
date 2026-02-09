package com.littlelemon.application.address.data

import android.location.Location
import app.cash.turbine.test
import com.littlelemon.application.address.data.local.AddressLocalDataSource
import com.littlelemon.application.address.data.mappers.toAddressEntity
import com.littlelemon.application.address.data.mappers.toLocalAddress
import com.littlelemon.application.address.data.remote.AddressRemoteDataSource
import com.littlelemon.application.core.domain.exceptions.LocationUnavailableException
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.utils.AddressGenerator
import com.littlelemon.application.utils.StandardTestDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(StandardTestDispatcherRule::class)
class AddressRepositoryImplTest {

    private val localDataSource = mockk<AddressLocalDataSource>()
    private val remoteDataSource = mockk<AddressRemoteDataSource>()

    private val repository = AddressRepositoryImpl(
        localDataSource,
        remoteDataSource
    )

    private companion object {
        const val LATITUDE = 1.2345
        const val LONGITUDE = 3.2354
    }

    @Test
    fun onGetLocation_dataSourceSuccess_returnsResourceSuccess() = runTest {
        // Arrange
        val location = mockk<Location>()
        every { location.latitude } returns LATITUDE
        every { location.longitude } returns LONGITUDE
        every { location.accuracy } returns 5.0f

        coEvery { localDataSource.getLocation() } returns location

        // Act
        val result = repository.getLocation()

        // Assert
        assertTrue(result is Resource.Success)
        val data = (result as Resource.Success).data
        assertNotNull(data)
        assertEquals(LATITUDE, data.latitude)
        assertEquals(LONGITUDE, data.longitude)
    }

    @Test
    fun onGetLocation_dataSourceError_returnsResourceError() = runTest {
        // Arrange
        coEvery { localDataSource.getLocation() } throws LocationUnavailableException()

        // Act
        val result = repository.getLocation()

        // Assert
        assertTrue(result is Resource.Failure)
    }

    @Test
    fun onGetAddress_remoteFailureLocalCacheNonEmpty_returnsSuccessWithLocalCache() = runTest {
        // Arrange
        val numAddress = 3
        val cachedAddress = List(numAddress) { AddressGenerator.generateAddressEntity() }
        coEvery { localDataSource.getAddress() } returns flow { emit(cachedAddress) }
        coEvery { remoteDataSource.getAddress() } throws Exception()

        // Act
        val resultFlow = repository.getAddress()

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)

            val firstResult = awaitItem()
            assertTrue(firstResult is Resource.Loading)
            firstResult as Resource.Loading
            assertEquals(cachedAddress.map { it.toLocalAddress() }, firstResult.data)

            val secondResult = awaitItem()
            assertTrue(secondResult is Resource.Failure)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun onGetAddress_remoteSuccess_returnsSuccessWithData() = runTest {
        // Arrange
        val numAddress = 3
        val remoteAddress = List(numAddress) { AddressGenerator.generateAddressDTO() }
        coEvery { localDataSource.getAddress() } returns flow { emit(emptyList()) }
        coEvery { remoteDataSource.getAddress() } returns remoteAddress

        // Act
        val resultFlow = repository.getAddress()

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)

            val firstResult = awaitItem()
            assertTrue(firstResult is Resource.Loading)
            firstResult as Resource.Loading
            assertNotNull(firstResult.data)
            assertTrue(firstResult.data.isEmpty())

            val secondResult = awaitItem()
            assertTrue(secondResult is Resource.Success)
            secondResult as Resource.Success
            assertNotNull(secondResult.data)
            assertEquals(
                remoteAddress.map { it.toAddressEntity().toLocalAddress() },
                secondResult.data
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onGetAddress_remoteSuccessBothEmpty_returnsSuccessWithEmptyData() = runTest {
        // Arrange
        coEvery { localDataSource.getAddress() } returns flow { emit(emptyList()) }
        coEvery { remoteDataSource.getAddress() } returns emptyList()

        // Act
        val resultFlow = repository.getAddress()

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)

            val firstResult = awaitItem()
            assertTrue(firstResult is Resource.Loading)
            firstResult as Resource.Loading
            assertNotNull(firstResult.data)
            assertTrue(firstResult.data.isEmpty())

            val secondResult = awaitItem()
            assertTrue(secondResult is Resource.Success)
            secondResult as Resource.Success
            assertNotNull(secondResult.data)
            assertTrue(secondResult.data.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onGetAddress_remoteFailureEmptyCache_returnsSuccessWithEmptyData() = runTest {
        // Arrange
        coEvery { localDataSource.getAddress() } returns flow { emit(emptyList()) }
        coEvery { remoteDataSource.getAddress() } throws Exception()

        // Act
        val resultFlow = repository.getAddress()

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)

            val firstResult = awaitItem()
            assertTrue(firstResult is Resource.Loading)
            firstResult as Resource.Loading
            assertNotNull(firstResult.data)
            assertTrue(firstResult.data.isEmpty())

            val secondResult = awaitItem()
            assertTrue(secondResult is Resource.Failure)
            secondResult as Resource.Failure

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onGetAddress_remoteFailureCacheFailure_returnsSuccessWithEmptyData() = runTest {
        // Arrange
        coEvery { localDataSource.getAddress() } throws IllegalStateException()
        coEvery { remoteDataSource.getAddress() } throws Exception()

        // Act
        val resultFlow = repository.getAddress()

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)

            val offlineResult = awaitItem()
            assertTrue(offlineResult is Resource.Failure)
            offlineResult as Resource.Failure
            assertNull(offlineResult.data)

            val remoteResult = awaitItem()
            assertTrue(remoteResult is Resource.Failure)
            remoteResult as Resource.Failure
            assertNull(remoteResult.data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onGetAddress_remoteSuccessNonEmptyCache_returnsSuccessWithEmptyData() = runTest {
        // Arrange
        val numAddress = 3
        val remoteAddress = List(numAddress) { AddressGenerator.generateAddressDTO() }
        val localAddress = List(numAddress) { AddressGenerator.generateAddressEntity() }
        coEvery { localDataSource.getAddress() } returns flow { emit(localAddress) }
        coEvery { remoteDataSource.getAddress() } returns remoteAddress

        // Act
        val resultFlow = repository.getAddress()

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)

            val firstResult = awaitItem()
            assertTrue(firstResult is Resource.Loading)
            firstResult as Resource.Loading
            assertEquals(localAddress.map { it.toLocalAddress() }, firstResult.data)

            val secondResult = awaitItem()
            assertTrue(secondResult is Resource.Success)
            secondResult as Resource.Success
            val expectedAddresses = localAddress.map { it.toLocalAddress() } + remoteAddress.map {
                it.toAddressEntity().toLocalAddress()
            }
            assertEquals(expectedAddresses, secondResult.data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onSaveAddress_remoteSuccess_returnsResourceSuccess() = runTest { }

    @Test
    fun onSaveAddress_remoteFailure_returnsResourceFailure() = runTest { }
}