package com.littlelemon.application.address.data.mappers

import android.location.Location
import com.littlelemon.application.utils.AddressGenerator
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

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

    @Nested
    inner class AddressEntityToLocalAddressTest() {

        @Test
        fun onConversion_addressWithoutLatLng_returnsCorrectLocalAddress() {
            // Arrange
            val addressEntity =
                AddressGenerator.generateAddressEntity().copy(latitude = null, longitude = null)
            // Act
            val localAddress = addressEntity.toLocalAddress()

            // Assert
            assertNull(localAddress.location)
            assertNotNull(localAddress.label)
            assertEquals(addressEntity.label, localAddress.label)
            assertNotNull(localAddress.address)
            assertEquals(addressEntity.address, localAddress.address?.address)
            assertEquals(addressEntity.streetAddress, localAddress.address?.streetAddress)
            assertEquals(addressEntity.city, localAddress.address?.city)
            assertEquals(addressEntity.state, localAddress.address?.state)
            assertEquals(addressEntity.pinCode, localAddress.address?.pinCode)
        }

        @Test
        fun onConversion_addressWithoutAddress_returnsCorrectLocalAddress() {
            // Arrange
            val addressEntity =
                AddressGenerator.generateAddressEntity()
                    .copy(address = null, streetAddress = null, city = null, state = null)
            // Act
            val localAddress = addressEntity.toLocalAddress()

            // Assert
            assertNotNull(localAddress.location)
            assertEquals(addressEntity.latitude, localAddress.location?.latitude)
            assertEquals(addressEntity.longitude, localAddress.location?.longitude)
            assertNotNull(localAddress.label)
            assertEquals(addressEntity.label, localAddress.label)
            assertNull(localAddress.address)
        }

        @Test
        fun onConversion_addressWithLocationAndLatLng_returnsCorrectLocalAddress() {
            // Arrange
            val addressEntity =
                AddressGenerator.generateAddressEntity()
            // Act
            val localAddress = addressEntity.toLocalAddress()

            // Assert
            assertNotNull(localAddress.location)
            assertEquals(addressEntity.latitude, localAddress.location?.latitude)
            assertEquals(addressEntity.longitude, localAddress.location?.longitude)
            assertNotNull(localAddress.label)
            assertEquals(addressEntity.label, localAddress.label)
            assertNotNull(localAddress.address)
            assertEquals(addressEntity.address, localAddress.address?.address)
            assertEquals(addressEntity.streetAddress, localAddress.address?.streetAddress)
            assertEquals(addressEntity.city, localAddress.address?.city)
            assertEquals(addressEntity.state, localAddress.address?.state)
            assertEquals(addressEntity.pinCode, localAddress.address?.pinCode)
        }

        // NOTE: Having both address and LatLng as null will throw an exception on initialization which is tested in the domain class layer itself.
    }
}