package com.littlelemon.application.address.data

import android.location.Location
import app.cash.turbine.test
import com.littlelemon.application.address.data.local.AddressLocalDataSource
import com.littlelemon.application.address.data.local.FakeAddressLocalDataSource
import com.littlelemon.application.address.data.local.dao.FakeGeocodingDao
import com.littlelemon.application.address.data.local.dao.GeocodingDao
import com.littlelemon.application.address.data.mappers.toAddressEntity
import com.littlelemon.application.address.data.mappers.toLocalAddress
import com.littlelemon.application.address.data.mappers.toRequestDTO
import com.littlelemon.application.address.data.remote.AddressRemoteDataSource
import com.littlelemon.application.address.data.remote.FakeAddressRemoteDataSource
import com.littlelemon.application.address.data.remote.FakeGeocodingRemoteDataSource
import com.littlelemon.application.address.data.remote.geocoding.GeocodingRemoteDataSource
import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.models.GeocodedAddress
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.address.utils.GeocodingGenerator
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
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertIs
import kotlin.time.Clock

@ExtendWith(StandardTestDispatcherRule::class)
class DefaultAddressRepositoryTest {

    private lateinit var addressLocalDataSource: AddressLocalDataSource
    private lateinit var addressRemoteDataSource: AddressRemoteDataSource
    private lateinit var geocodingLocalDataSource: GeocodingDao
    private lateinit var geocodingRemoteDataSource: GeocodingRemoteDataSource

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
        addressLocalDataSource = FakeAddressLocalDataSource(location = location)
        addressRemoteDataSource = FakeAddressRemoteDataSource()
        geocodingLocalDataSource = FakeGeocodingDao()
        geocodingRemoteDataSource = FakeGeocodingRemoteDataSource()
        repository = DefaultAddressRepository(
            addressLocalDataSource,
            addressRemoteDataSource,
            geocodingLocalDataSource,
            geocodingRemoteDataSource
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
        addressLocalDataSource = FakeAddressLocalDataSource(throwError = true)
        repository = DefaultAddressRepository(
            addressLocalDataSource,
            addressRemoteDataSource,
            geocodingLocalDataSource,
            geocodingRemoteDataSource
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
        addressLocalDataSource = FakeAddressLocalDataSource(initialData = cachedAddress)
        addressRemoteDataSource = FakeAddressRemoteDataSource(throwError = true)
        repository = DefaultAddressRepository(
            addressLocalDataSource,
            addressRemoteDataSource,
            geocodingLocalDataSource,
            geocodingRemoteDataSource
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
        addressLocalDataSource = FakeAddressLocalDataSource()
        addressRemoteDataSource = FakeAddressRemoteDataSource(initialData = remoteAddress)
        repository = DefaultAddressRepository(
            addressLocalDataSource,
            addressRemoteDataSource,
            geocodingLocalDataSource,
            geocodingRemoteDataSource
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
        addressLocalDataSource = FakeAddressLocalDataSource()
        addressRemoteDataSource = FakeAddressRemoteDataSource()
        repository = DefaultAddressRepository(
            addressLocalDataSource,
            addressRemoteDataSource,
            geocodingLocalDataSource,
            geocodingRemoteDataSource
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
        addressLocalDataSource = FakeAddressLocalDataSource()
        addressRemoteDataSource = FakeAddressRemoteDataSource(throwError = true)
        repository = DefaultAddressRepository(
            addressLocalDataSource,
            addressRemoteDataSource,
            geocodingLocalDataSource,
            geocodingRemoteDataSource
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
        addressLocalDataSource = FakeAddressLocalDataSource(throwError = true)
        addressRemoteDataSource = FakeAddressRemoteDataSource(throwError = true)
        repository = DefaultAddressRepository(
            addressLocalDataSource,
            addressRemoteDataSource,
            geocodingLocalDataSource,
            geocodingRemoteDataSource
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
        addressLocalDataSource = FakeAddressLocalDataSource(initialData = localAddress)
        addressRemoteDataSource = FakeAddressRemoteDataSource(initialData = remoteAddress)
        repository = DefaultAddressRepository(
            addressLocalDataSource,
            addressRemoteDataSource,
            geocodingLocalDataSource,
            geocodingRemoteDataSource
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
    fun onSaveAddress_newAddressAndRemoteSuccess_returnsResourceSuccessAndPersistsInCache() =
        runTest {
            // Arrange
            val address = AddressGenerator.generateLocalAddress().copy(id = null)

            // Act
            val resource = repository.saveAddress(address)
            val genId = addressRemoteDataSource.getAddress().first().id
            val data = addressLocalDataSource.getAddress().first()

            // Assert
            assertTrue(resource is Resource.Success)
            assertEquals(address.copy(id = genId), data.first().toLocalAddress())
        }

    @Test
    fun onSaveAddress_existingAddressAndRemoteSuccess_returnsResourceSuccessAndPersistsInCache() =
        runTest {
            // Arrange
            val address = AddressGenerator.generateLocalAddress()
            addressRemoteDataSource.saveAddress(address.toRequestDTO())
            val updatedAddress =
                address.copy(label = "updated label", location = LocalLocation(1.23455, 3.5123412))

            // Act
            val resource = repository.saveAddress(updatedAddress)
            val data = addressLocalDataSource.getAddress().first()

            // Assert
            assertTrue(resource is Resource.Success)
            assertEquals(updatedAddress, data.first().toLocalAddress())
        }

    @Test
    fun onSaveAddress_remoteFailure_returnsResourceFailureAndDoesNotSaveToCache() = runTest {
        // Arrange
        val address = AddressGenerator.generateLocalAddress()
        addressRemoteDataSource = FakeAddressRemoteDataSource(throwError = true)
        repository = DefaultAddressRepository(
            addressLocalDataSource,
            addressRemoteDataSource,
            geocodingLocalDataSource,
            geocodingRemoteDataSource
        )

        // Act
        val resource = repository.saveAddress(address)

        // Assert
        assertTrue(resource is Resource.Failure)
        assertTrue(addressLocalDataSource.getAddress().first().isEmpty())
    }

    @Test
    fun getAddressCount_removeDataSourceReturnsCount_returnsCount() = runTest {
        val expectedAddressCount = 3
        val addresses = List(expectedAddressCount) { AddressGenerator.generateAddressDTO() }
        addressRemoteDataSource = FakeAddressRemoteDataSource(addresses)
        repository = DefaultAddressRepository(
            addressLocalDataSource,
            addressRemoteDataSource,
            geocodingLocalDataSource,
            geocodingRemoteDataSource
        )

        val addressCount = repository.getAddressCount()
        assertEquals(expectedAddressCount, addressCount)
    }

    @Test
    fun getAddressCount_localDatasourceReturnsZero_returnsZero() = runTest {
        // Given local data source contains zero addresses

        // When getAddressCount is called
        val addressCount = repository.getAddressCount()

        // Then, 0 is returned
        assertEquals(0, addressCount)
    }

    @Test
    fun getAddressCount_localDataSourceReturnsNonZero_returnsNonZero() = runTest {
        // Given local data source contains five addresses
        val expectedAddressCount = 5
        val addresses = List(expectedAddressCount) { AddressGenerator.generateAddressEntity() }
        addressLocalDataSource = FakeAddressLocalDataSource(addresses)
        repository = DefaultAddressRepository(
            addressLocalDataSource, addressRemoteDataSource,
            geocodingLocalDataSource,
            geocodingRemoteDataSource
        )

        // When getAddressCount is called
        val addressCount = repository.getAddressCount()

        // Then, five is returned
        assertEquals(expectedAddressCount.toLong(), addressCount)
    }

    @Test
    fun getAddressCount_localDataSourceError_returnsNegativeOne() = runTest {
        // Given local data source error
        addressLocalDataSource = FakeAddressLocalDataSource(throwError = true)
        repository = DefaultAddressRepository(
            addressLocalDataSource, addressRemoteDataSource,
            geocodingLocalDataSource,
            geocodingRemoteDataSource
        )

        // When getAddressCount is called
        val addressCount = repository.getAddressCount()

        // Then, -1 is returned
        assertEquals(-1, addressCount)
    }

    @Nested
    inner class GeocodingTests {

        @Test
        fun gecodeAddress_firstTimeGeocode_returnsCorrectAddress() = runTest {
            // When address is geocoded for the first time
            val (_, local, dto) = GeocodingGenerator.generateGeocodingEntities()
            geocodingRemoteDataSource = FakeGeocodingRemoteDataSource(dto)
            repository = DefaultAddressRepository(
                addressLocalDataSource, addressRemoteDataSource,
                geocodingLocalDataSource,
                geocodingRemoteDataSource
            )
            val actualGeocodedResult = repository.geocodeAddress(dto.fullAddress)

            // Then, then a valid result is returned from the server
            assertIs<Resource.Success<GeocodedAddress>>(actualGeocodedResult)
            assertEquals(local, actualGeocodedResult.data)
        }

        @Test
        fun geocodeAddress_validCache_returnsAddressFromCache() = runTest {
            // Given valid cache
            val (entity, local, _) = GeocodingGenerator.generateGeocodingEntities()
            geocodingLocalDataSource = FakeGeocodingDao(
                listOf(
                    entity.copy(
                        createdTimestamp = Clock.System.now().toEpochMilliseconds()
                    )
                )
            )
            repository = DefaultAddressRepository(
                addressLocalDataSource, addressRemoteDataSource,
                geocodingLocalDataSource,
                geocodingRemoteDataSource
            )

            // When address is geocoded
            val actualGeocodedResult = repository.geocodeAddress(entity.fullAddress)

            // Then, a valid result is returned from the cache
            assertIs<Resource.Success<GeocodedAddress>>(actualGeocodedResult)
            assertEquals(local, actualGeocodedResult.data)
        }

        @Test
        fun gecodeAddress_expiredCache_returnsAddressFromRemote() = runTest {
            // Given expired local cache
            val fortyFiveDays = 45 * 24 * 60 * 60 * 1000L
            val (entity, local, dto) = GeocodingGenerator.generateGeocodingEntities()
            geocodingLocalDataSource = FakeGeocodingDao(
                listOf(
                    entity.copy(
                        createdTimestamp = Clock.System.now().toEpochMilliseconds() - fortyFiveDays
                    )
                )
            )
            geocodingRemoteDataSource =
                FakeGeocodingRemoteDataSource(dto.copy(fullAddress = "Remote Address"))
            repository = DefaultAddressRepository(
                addressLocalDataSource, addressRemoteDataSource,
                geocodingLocalDataSource,
                geocodingRemoteDataSource
            )

            // When address is geocoded
            val actualGeocodedResult = repository.geocodeAddress(dto.fullAddress)

            // Then, result is fetched from the server
            assertIs<Resource.Success<GeocodedAddress>>(actualGeocodedResult)
            assertEquals(local.copy(fullAddress = "Remote Address"), actualGeocodedResult.data)
        }

        @Test
        fun geocodeAddress_remoteFailure_returnsSuccessWithCachedData() = runTest {
            // Given remote failure with non-stale cache
            val (entity, local, dto) = GeocodingGenerator.generateGeocodingEntities()
            geocodingLocalDataSource = FakeGeocodingDao(
                listOf(
                    entity.copy(
                        createdTimestamp = Clock.System.now().toEpochMilliseconds()
                    )
                )
            )
            geocodingRemoteDataSource =
                FakeGeocodingRemoteDataSource(throwError = true)
            repository = DefaultAddressRepository(
                addressLocalDataSource, addressRemoteDataSource,
                geocodingLocalDataSource,
                geocodingRemoteDataSource
            )

            // When address is geocoded
            val actualGeocodedResult = repository.geocodeAddress(dto.fullAddress)

            // Then, a valid result is returned from the cache with remote success
            assertIs<Resource.Success<GeocodedAddress>>(actualGeocodedResult)
            assertEquals(local, actualGeocodedResult.data)
        }

        @Test
        fun geocodeAddress_remoteFailureAndExpiredCache_returnsFailureWithNullData() = runTest {
            // Given remote failure and expired cache
            val fortyFiveDays = 45 * 24 * 60 * 60 * 1000L
            val (entity, _, dto) = GeocodingGenerator.generateGeocodingEntities()
            geocodingLocalDataSource = FakeGeocodingDao(
                listOf(
                    entity.copy(
                        createdTimestamp = Clock.System.now().toEpochMilliseconds() - fortyFiveDays
                    )
                )
            )
            geocodingRemoteDataSource =
                FakeGeocodingRemoteDataSource(throwError = true)
            repository = DefaultAddressRepository(
                addressLocalDataSource, addressRemoteDataSource,
                geocodingLocalDataSource,
                geocodingRemoteDataSource
            )

            // When address is geocoded
            val actualGeocodedResult = repository.geocodeAddress(dto.fullAddress)

            // Then, failure is returned
            assertIs<Resource.Failure<GeocodedAddress>>(actualGeocodedResult)
            kotlin.test.assertNull(actualGeocodedResult.data)
        }

        @Test
        fun geocodeAddress_cacheFailure_returnsFailureWithNullData() = runTest {
            // Given cache failure
            val (_, _, dto) = GeocodingGenerator.generateGeocodingEntities()
            geocodingLocalDataSource = FakeGeocodingDao(throwError = true)
            geocodingRemoteDataSource =
                FakeGeocodingRemoteDataSource(returnValue = dto)
            repository = DefaultAddressRepository(
                addressLocalDataSource, addressRemoteDataSource,
                geocodingLocalDataSource,
                geocodingRemoteDataSource
            )

            // When address is geocoded
            val actualGeocodedResult = repository.geocodeAddress(dto.fullAddress)

            // Then, failure is returned
            assertIs<Resource.Failure<GeocodedAddress>>(actualGeocodedResult)
            kotlin.test.assertNull(actualGeocodedResult.data)
        }

    }

    @Nested
    inner class ReverseGeocodingTests {

        @Test
        fun reverseGecodeAddress_firstTimeGeocode_returnsCorrectAddress() = runTest {
            // When address is reverse geocoded for the first time
            val (entity, local, dto) = GeocodingGenerator.generateGeocodingEntities()
            geocodingRemoteDataSource = FakeGeocodingRemoteDataSource(dto)
            repository = DefaultAddressRepository(
                addressLocalDataSource, addressRemoteDataSource,
                geocodingLocalDataSource,
                geocodingRemoteDataSource
            )
            val actualGeocodedResult = repository.reverseGeocodeLocation(local.location!!)

            // Then, then a valid result is returned from the server
            assertIs<Resource.Success<GeocodedAddress>>(actualGeocodedResult)
            assertEquals(local, actualGeocodedResult.data)
        }

        @Test
        fun reverseGeocodeAddress_validCache_returnsAddressFromCache() = runTest {
            // Given valid cache
            val (entity, local, _) = GeocodingGenerator.generateGeocodingEntities()
            geocodingLocalDataSource = FakeGeocodingDao(
                listOf(
                    entity.copy(
                        createdTimestamp = Clock.System.now().toEpochMilliseconds()
                    )
                )
            )
            repository = DefaultAddressRepository(
                addressLocalDataSource, addressRemoteDataSource,
                geocodingLocalDataSource,
                geocodingRemoteDataSource
            )

            // When latlng is reverse geocoded
            val actualGeocodedResult = repository.reverseGeocodeLocation(local.location!!)

            // Then, a valid result is returned from the cache
            assertIs<Resource.Success<GeocodedAddress>>(actualGeocodedResult)
            assertEquals(local, actualGeocodedResult.data)
        }

        @Test
        fun reverseGecodeAddress_expiredCache_returnsAddressFromRemote() = runTest {
            // Given expired local cache
            val fortyFiveDays = 45 * 24 * 60 * 60 * 1000L
            val (entity, local, dto) = GeocodingGenerator.generateGeocodingEntities()
            geocodingLocalDataSource = FakeGeocodingDao(
                listOf(
                    entity.copy(
                        createdTimestamp = Clock.System.now().toEpochMilliseconds() - fortyFiveDays
                    )
                )
            )
            geocodingRemoteDataSource =
                FakeGeocodingRemoteDataSource(dto.copy(fullAddress = "Remote Address"))
            repository = DefaultAddressRepository(
                addressLocalDataSource, addressRemoteDataSource,
                geocodingLocalDataSource,
                geocodingRemoteDataSource
            )

            // When latlng is reverse geocoded
            val actualGeocodedResult = repository.reverseGeocodeLocation(local.location!!)

            // Then, result is fetched from the server
            assertIs<Resource.Success<GeocodedAddress>>(actualGeocodedResult)
            assertEquals(local.copy(fullAddress = "Remote Address"), actualGeocodedResult.data)
        }

        @Test
        fun reverseGeocodeAddress_remoteFailure_returnsSuccessWithCachedData() = runTest {
            // Given remote failure with non-stale cache
            val (entity, local, dto) = GeocodingGenerator.generateGeocodingEntities()
            geocodingLocalDataSource = FakeGeocodingDao(
                listOf(
                    entity.copy(
                        createdTimestamp = Clock.System.now().toEpochMilliseconds()
                    )
                )
            )
            geocodingRemoteDataSource =
                FakeGeocodingRemoteDataSource(throwError = true)
            repository = DefaultAddressRepository(
                addressLocalDataSource, addressRemoteDataSource,
                geocodingLocalDataSource,
                geocodingRemoteDataSource
            )

            // When latlng is reverse geocoded
            val actualGeocodedResult = repository.reverseGeocodeLocation(local.location!!)

            // Then, a valid result is returned from the cache with remote success
            assertIs<Resource.Success<GeocodedAddress>>(actualGeocodedResult)
            assertEquals(local, actualGeocodedResult.data)
        }

        @Test
        fun reverseGeocodeAddress_remoteFailureAndExpiredCache_returnsFailureWithNullData() =
            runTest {
                // Given remote failure and expired cache
                val fortyFiveDays = 45 * 24 * 60 * 60 * 1000L
                val (entity, local, _) = GeocodingGenerator.generateGeocodingEntities()
                geocodingLocalDataSource = FakeGeocodingDao(
                    listOf(
                        entity.copy(
                            createdTimestamp = Clock.System.now()
                                .toEpochMilliseconds() - fortyFiveDays
                        )
                    )
                )
                geocodingRemoteDataSource =
                    FakeGeocodingRemoteDataSource(throwError = true)
                repository = DefaultAddressRepository(
                    addressLocalDataSource, addressRemoteDataSource,
                    geocodingLocalDataSource,
                    geocodingRemoteDataSource
                )

                // When latlng is reverse geocoded
                val actualGeocodedResult = repository.reverseGeocodeLocation(local.location!!)

                // Then, failure is returned
                assertIs<Resource.Failure<GeocodedAddress>>(actualGeocodedResult)
                kotlin.test.assertNull(actualGeocodedResult.data)
            }

        @Test
        fun reverseGeocodeAddress_cacheFailure_returnsFailureWithNullData() = runTest {
            // Given cache failure
            val (_, local, dto) = GeocodingGenerator.generateGeocodingEntities()
            geocodingLocalDataSource = FakeGeocodingDao(throwError = true)
            geocodingRemoteDataSource =
                FakeGeocodingRemoteDataSource(returnValue = dto)
            repository = DefaultAddressRepository(
                addressLocalDataSource, addressRemoteDataSource,
                geocodingLocalDataSource,
                geocodingRemoteDataSource
            )

            // When latlng is reverse geocoded
            val actualGeocodedResult = repository.reverseGeocodeLocation(local.location!!)

            // Then, failure is returned
            assertIs<Resource.Failure<GeocodedAddress>>(actualGeocodedResult)
            kotlin.test.assertNull(actualGeocodedResult.data)
        }

    }

    @Nested
    inner class GetCurrentAddress {

        @Test
        fun defaultAddressPresent_returnsDefaultAddress() = runTest {
            // Given an address repository with a default address.
            val nonDefaultAddresses =
                List(3) { AddressGenerator.generateAddressEntity().copy(isDefault = false) }
            val expectedDefaultAddress =
                AddressGenerator.generateAddressEntity().copy(isDefault = true)
            addressLocalDataSource =
                FakeAddressLocalDataSource(
                    initialData = nonDefaultAddresses + listOf(
                        expectedDefaultAddress
                    )
                )
            repository = DefaultAddressRepository(
                addressLocalDataSource,
                addressRemoteDataSource,
                geocodingLocalDataSource,
                geocodingRemoteDataSource
            )

            // When getDefaultAddressIsCalled
            val defaultAddress = repository.getCurrentAddress().first()

            // Then, it returns the default address with success
            assertIs<Resource.Success<LocalAddress>>(defaultAddress)
            assertEquals(expectedDefaultAddress.toLocalAddress(), defaultAddress.data)
        }


        @Test
        fun multipleDefault_returnsFirstDefaultAddress() = runTest {
            // Given an address repository with more than one default addresses.
            val defaultAddresses =
                List(3) { AddressGenerator.generateAddressEntity().copy(isDefault = true) }
            addressLocalDataSource =
                FakeAddressLocalDataSource(
                    initialData = defaultAddresses
                )
            repository = DefaultAddressRepository(
                addressLocalDataSource,
                addressRemoteDataSource,
                geocodingLocalDataSource,
                geocodingRemoteDataSource
            )

            // When getDefaultAddressIsCalled
            val defaultAddress = repository.getCurrentAddress().first()

            // Then, it returns the default address with success
            assertIs<Resource.Success<LocalAddress>>(defaultAddress)
            assertEquals(defaultAddresses.first().toLocalAddress(), defaultAddress.data)
        }

        @Test
        fun nonEmptyCacheWithNoDefault_returnsFirstAddress() = runTest {
            // Given a non-empty address repository with no default address.
            val nonDefaultAddresses =
                List(3) { AddressGenerator.generateAddressEntity().copy(isDefault = false) }
            addressLocalDataSource =
                FakeAddressLocalDataSource(
                    initialData = nonDefaultAddresses
                )
            repository = DefaultAddressRepository(
                addressLocalDataSource,
                addressRemoteDataSource,
                geocodingLocalDataSource,
                geocodingRemoteDataSource
            )

            // When getDefaultAddressIsCalled
            val defaultAddress = repository.getCurrentAddress().first()
            print(repository.getAddress().first())
            print(defaultAddress)

            // Then, it returns the default address with success
            assertIs<Resource.Success<LocalAddress>>(defaultAddress)
            assertEquals(nonDefaultAddresses.first().toLocalAddress(), defaultAddress.data)
        }

        @Test
        fun emptyCache_returnSuccessWithNull() = runTest {
            // Given an empty address repository with no default address.
            addressLocalDataSource =
                FakeAddressLocalDataSource(
                    initialData = emptyList()
                )
            repository = DefaultAddressRepository(
                addressLocalDataSource,
                addressRemoteDataSource,
                geocodingLocalDataSource,
                geocodingRemoteDataSource
            )

            // When getDefaultAddressIsCalled
            val defaultAddress = repository.getCurrentAddress().first()

            // Then, it returns the null with success
            assertIs<Resource.Success<LocalAddress>>(defaultAddress)
            assertNull(defaultAddress.data)
        }
    }
}