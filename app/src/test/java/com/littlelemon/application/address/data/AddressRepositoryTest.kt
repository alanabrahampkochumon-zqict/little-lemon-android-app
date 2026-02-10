package com.littlelemon.application.address.data

import android.location.Location
import app.cash.turbine.test
import com.littlelemon.application.address.data.local.AddressLocalDataSource
import com.littlelemon.application.address.data.local.FakeAddressLocalDataSource
import com.littlelemon.application.address.data.mappers.toAddressEntity
import com.littlelemon.application.address.data.mappers.toLocalAddress
import com.littlelemon.application.address.data.remote.AddressRemoteDataSource
import com.littlelemon.application.address.data.remote.FakeAddressRemoteDataSource
import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.utils.AddressGenerator
import com.littlelemon.application.utils.StandardTestDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(StandardTestDispatcherRule::class)
class AddressRepositoryTest {

    private lateinit var localDataSource: AddressLocalDataSource
    private lateinit var remoteDataSource: AddressRemoteDataSource

    private lateinit var repository: AddressRepository

    private companion object {
        const val LATITUDE = 1.2345
        const val LONGITUDE = 3.2354
    }

    @BeforeEach
    fun setUp() {
        val location = mockk<Location>()
        every { location.latitude } returns LATITUDE
        every { location.longitude } returns LONGITUDE
        every { location.accuracy } returns 5.0f
        localDataSource = FakeAddressLocalDataSource(location = location)
        remoteDataSource = FakeAddressRemoteDataSource()
        repository = AddressRepositoryImpl(
            localDataSource,
            remoteDataSource
        )
    }

    @Test
    fun onGetLocation_dataSourceSuccess_returnsResourceSuccess() = runTest {
        // Arrange
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
        localDataSource = FakeAddressLocalDataSource(throwError = true)
        repository = AddressRepositoryImpl(
            localDataSource,
            remoteDataSource
        )

        // Act
        val result = repository.getLocation()

        // Assert
        assertTrue(result is Resource.Failure)
    }

    @Test
    fun onGetAddress_remoteFailureLocalCacheNonEmpty_returnsFailureWithLocalCache() = runTest {
        // Arrange
        val numAddress = 3
        val cachedAddress = List(numAddress) { AddressGenerator.generateAddressEntity() }
        localDataSource = FakeAddressLocalDataSource(initialData = cachedAddress)
        remoteDataSource = FakeAddressRemoteDataSource(throwError = true)
        repository = AddressRepositoryImpl(
            localDataSource,
            remoteDataSource
        )

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
        localDataSource = FakeAddressLocalDataSource()
        remoteDataSource = FakeAddressRemoteDataSource(initialData = remoteAddress)
        repository = AddressRepositoryImpl(
            localDataSource,
            remoteDataSource
        )

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
        localDataSource = FakeAddressLocalDataSource()
        remoteDataSource = FakeAddressRemoteDataSource()
        repository = AddressRepositoryImpl(
            localDataSource,
            remoteDataSource
        )

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
    fun onGetAddress_remoteFailureEmptyCache_returnsFailureWithEmptyData() = runTest {
        // Arrange
        localDataSource = FakeAddressLocalDataSource()
        remoteDataSource = FakeAddressRemoteDataSource(throwError = true)
        repository = AddressRepositoryImpl(
            localDataSource,
            remoteDataSource
        )

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
    fun onGetAddress_remoteFailureCacheFailure_returnsFailureWithoutNetworkCall() = runTest {
        // Arrange
        localDataSource = FakeAddressLocalDataSource(throwError = true)
        remoteDataSource = FakeAddressRemoteDataSource(throwError = true)
        repository = AddressRepositoryImpl(
            localDataSource,
            remoteDataSource
        )

        // Act
        val resultFlow = repository.getAddress()

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)

            val offlineResult = awaitItem()
            assertTrue(offlineResult is Resource.Failure)
            offlineResult as Resource.Failure
            assertNull(offlineResult.data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onGetAddress_remoteSuccessNonEmptyCache_returnsSuccessWithEmptyData() = runTest {
        // Arrange
        val numAddress = 3
        val remoteAddress = List(numAddress) { AddressGenerator.generateAddressDTO() }
        val localAddress = List(numAddress) { AddressGenerator.generateAddressEntity() }
        localDataSource = FakeAddressLocalDataSource(initialData = localAddress)
        remoteDataSource = FakeAddressRemoteDataSource(initialData = remoteAddress)
        repository = AddressRepositoryImpl(
            localDataSource,
            remoteDataSource
        )

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
    fun onSaveAddress_remoteSuccess_returnsResourceSuccessAndPersistsInCache() = runTest {
        // Arrange
        val address = AddressGenerator.generateLocalAddress()

        // Act
        val resource = repository.saveAddress(address)
        val data = localDataSource.getAddress().first()

        // Assert
        assertTrue(resource is Resource.Success)
        assertEquals(address, data.first().toLocalAddress())
    }

    @Test
    fun onSaveAddress_remoteFailure_returnsResourceFailureAndDoesNotSaveToCache() = runTest {
        // Arrange
        val address = AddressGenerator.generateLocalAddress()
        remoteDataSource = FakeAddressRemoteDataSource(throwError = true)
        repository = AddressRepositoryImpl(
            localDataSource,
            remoteDataSource
        )

        // Act
        val resource = repository.saveAddress(address)

        // Assert
        assertTrue(resource is Resource.Failure)
        assertTrue(localDataSource.getAddress().first().isEmpty())
    }
}