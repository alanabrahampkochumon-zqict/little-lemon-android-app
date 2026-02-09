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
        val latitude = 1.2345
        val longitude = 3.2354

        @Test
        fun onLocationMapped_localLocationInstanceReturned() {
            // Arrange
            val location = mockk<Location>()
            every { location.latitude } returns latitude
            every { location.longitude } returns longitude

            // Act
            val localLocation = location.toLocalLocation()

            // Assert
            assertNotNull(localLocation)
            assertEquals(latitude, localLocation.latitude)
            assertEquals(longitude, localLocation.longitude)
        }
    }

    @Nested
    inner class AddressEntityToLocalAddressTest {

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

    @Nested
    inner class AddressDTOToEntityTests {

        @Test
        fun onConversion_withNonNullFields_returnsCorrectEntity() {
            // Arrange
            val addressDTO = AddressGenerator.generateAddressDTO()

            // Act
            val result = addressDTO.toAddressEntity()

            // Assert
            assertEquals(addressDTO.id, result.id)
            assertEquals(addressDTO.label, result.label)
            assertEquals(addressDTO.address, result.address)
            assertEquals(addressDTO.streetAddress, result.streetAddress)
            assertEquals(addressDTO.city, result.city)
            assertEquals(addressDTO.state, result.state)
            assertEquals(addressDTO.pinCode, result.pinCode)
            assertEquals(addressDTO.latitude, result.latitude)
            assertEquals(addressDTO.longitude, result.longitude)
            assertEquals(addressDTO.createdAt, result.createdAt)
        }
    }

    @Nested
    inner class AddressDTOToAddressRequestDTOTests {

        @Test
        fun onConversion_withNullLatLng_returnsRequestDTOWithNullLocation() {
            // Arrange
            val dto = AddressGenerator.generateAddressDTO().copy(latitude = null, longitude = null)

            // Act
            val requestDTO = dto.toRequestDTO()

            // Assert
            assertEquals(dto.id, requestDTO.id)
            assertEquals(dto.label, requestDTO.label)
            assertEquals(dto.address, requestDTO.address)
            assertEquals(dto.streetAddress, requestDTO.streetAddress)
            assertEquals(dto.city, requestDTO.city)
            assertEquals(dto.state, requestDTO.state)
            assertEquals(dto.pinCode, requestDTO.pinCode)
            assertEquals(dto.createdAt, requestDTO.createdAt)

            assertNull(requestDTO.location)
        }

        @Test
        fun onConversion_withNonNullLatLng_returnsRequestDTOWithCorrectLocation() {
            // Arrange
            val dto = AddressGenerator.generateAddressDTO().copy()

            // Act
            val requestDTO = dto.toRequestDTO()

            // Assert
            assertEquals(dto.id, requestDTO.id)
            assertEquals(dto.label, requestDTO.label)
            assertEquals(dto.address, requestDTO.address)
            assertEquals(dto.streetAddress, requestDTO.streetAddress)
            assertEquals(dto.city, requestDTO.city)
            assertEquals(dto.state, requestDTO.state)
            assertEquals(dto.pinCode, requestDTO.pinCode)
            assertEquals(dto.createdAt, requestDTO.createdAt)

            assertNotNull(requestDTO.location)
            assertEquals("POINT(${dto.longitude} ${dto.latitude})", requestDTO.location)
        }
    }
}