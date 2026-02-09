package com.littlelemon.application.address.data.local

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Tasks
import com.littlelemon.application.address.data.local.dao.AddressDao
import com.littlelemon.application.utils.AddressGenerator
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows

class AddressLocalDataSourceTests {

    private companion object {
        const val LATITUDE = 1.2343
        const val NEW_LATITUDE = 2.344343
        const val LONGITUDE = 3.23452
        const val NEW_LONGITUDE = 4.234234

        const val STALE_TIME = 10 * 24 * 60 * 60 * 1000L
    }

    private val locationProvider = mockk<FusedLocationProviderClient>()
    private val dao = mockk<AddressDao>()
    private val newLocation = mockk<Location>(relaxed = true)
    private val staleLocation = mockk<Location>(relaxed = true)
    private val datasource = AddressLocalDataSourceImpl(locationProvider, dao)

    @BeforeEach
    fun setUp() {
        every { newLocation.latitude } returns NEW_LATITUDE
        every { newLocation.longitude } returns NEW_LONGITUDE
        every { newLocation.time } returns System.currentTimeMillis()

        every { staleLocation.latitude } returns LATITUDE
        every { staleLocation.longitude } returns LONGITUDE
        every { staleLocation.time } returns System.currentTimeMillis() - STALE_TIME
    }

    @Test
    fun onGetLastLocation_nullLocation_getNewLocation() = runTest {
        // Arrange
        every { locationProvider.lastLocation } returns Tasks.forResult<Location?>(null)
        every {
            locationProvider.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            )
        } returns Tasks.forResult(newLocation)

        newLocation.time = System.currentTimeMillis()
        newLocation.elapsedRealtimeNanos = System.currentTimeMillis() * 1_000

        // Act
        val result = datasource.getLocation()

        // Assert
        assertNotNull(result)
        assertEquals(NEW_LATITUDE, result.latitude)
        assertEquals(NEW_LONGITUDE, result.longitude)
        assertTrue(result.time > System.currentTimeMillis() - STALE_TIME)
    }

    @Test
    fun onGetLastLocation_staleLocation_getNewLocation() = runTest {
        // Arrange
        every { locationProvider.lastLocation } returns Tasks.forResult(staleLocation)
        every { locationProvider.getCurrentLocation(any<Int>(), null) } returns Tasks.forResult(
            newLocation
        )

        // Act
        val result = datasource.getLocation()

        // Assert
        assertNotNull(result)
        assertEquals(newLocation.latitude, result.latitude)
        assertEquals(newLocation.longitude, result.longitude)
        assertEquals(newLocation.time, result.time)

    }

    @Test
    fun onGetLastLocation_freshLocation_getsLastLocation() = runTest {
        // Arrange
        every { locationProvider.lastLocation } returns Tasks.forResult(newLocation)
        // Act
        val result = datasource.getLocation()

        // Assert
        assertNotNull(result)
        assertEquals(NEW_LATITUDE, result.latitude)
        assertEquals(NEW_LONGITUDE, result.longitude)
        assertTrue { result.time > System.currentTimeMillis() - STALE_TIME }
    }

    @Test
    fun onGetAddress_noAddress_returnsFlowOfEmptyList() = runTest {
        // Arrange
        coEvery { dao.getAllAddress() } returns flow { emit(emptyList()) }

        // Act
        val result = datasource.getAddress().first()

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun onGetAddress_validAddresses_returnsFlowOfEmptyList() = runTest {
        // Arrange
        val numAddress = 5
        val address = List(numAddress) { AddressGenerator.generateAddressEntity() }
        coEvery { dao.getAllAddress() } returns flow { emit(address) }

        // Act
        val result = datasource.getAddress().first()

        // Assert
        assertEquals(address, result)
    }

    @Test
    fun onGetAddress_exception_returnsFlowOfEmptyList() = runTest {
        // Arrange
        coEvery { dao.getAllAddress() } throws IllegalStateException()

        // Act & Assert
        assertThrows<IllegalStateException> { datasource.getAddress().collect {} }
    }

    @Test
    fun onInsertAddress_validAddresses_throwsNoException() = runTest {
        // Arrange
        val address = AddressGenerator.generateAddressEntity()
        coEvery { dao.insertAddress(address) } returns Unit

        // Act & Assert
        assertDoesNotThrow { datasource.saveAddress(address) }
    }

    @Test
    fun onInsertAddress_exceptionThrown_throwsNoException() = runTest {
        // Arrange
        coEvery { dao.getAllAddress() } throws IllegalStateException()

        // Act & Assert
        assertThrows<IllegalStateException> { datasource.getAddress().first() }
    }
}