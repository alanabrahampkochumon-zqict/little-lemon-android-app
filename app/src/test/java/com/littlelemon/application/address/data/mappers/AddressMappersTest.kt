package com.littlelemon.application.address.data.mappers

import android.location.Location
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class AddressMappersTest {
    @Nested
    inner class ToLocalLocationTest() {
        val LATITUDE = 1.2345
        val LONGITUDE = 3.2354

        @Test
        fun onLocationMapped_localLocationInstanceReturned() {
            // Arrange
            val location = mockk<Location>()
            every { location.latitude } returns LATITUDE
            every { location.longitude } returns LONGITUDE

            // Act
            val localLocation = location.toLocalLocation()

            // Assert
            assertNotNull(localLocation)
            assertEquals(LATITUDE, localLocation.latitude)
            assertEquals(LONGITUDE, localLocation.longitude)
        }
    }
}