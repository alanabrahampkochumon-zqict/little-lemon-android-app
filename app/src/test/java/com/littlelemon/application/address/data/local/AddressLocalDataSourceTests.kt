package com.littlelemon.application.address.data.local

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Tasks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

class AddressLocalDataSourceTests {

    private companion object {
        const val LATITUDE = 1.2343
        const val NEW_LATITUDE = 2.344343
        const val LONGITUDE = 3.23452
        const val NEW_LONGITUDE = 4.234234

        const val STALE_TIME = 10 * 24 * 60 * 60 * 1000L
    }

    private val locationProvider = mockk<FusedLocationProviderClient>()
    private val newLocation = mockk<Location>(relaxed = true)
    private val staleLocation = mockk<Location>(relaxed = true)
    private val datasource = AddressLocalDataSourceImpl(locationProvider)

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
    fun getLastLocation_null_getNewLocation() = runTest {
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
    fun getLastLocation_stale_getNewLocation() = runTest {
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
    fun getLastLocation_fresh_getsLastLocation() = runTest {
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

}