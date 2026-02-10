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
import org.junit.jupiter.api.assertThrows

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
            val dto = AddressGenerator.generateAddressDTO()

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

    @Nested
    inner class AddressRequestToAddressDTOTests {

        @Test
        fun onConversion_withNullId_throwsIllegalArgumentException() {
            // Arrange
            val dto = AddressGenerator.generateAddressDTO().toRequestDTO().copy(id = null)

            // Act
            assertThrows<IllegalArgumentException> { dto.toResponse() }
        }

        @Test
        fun onConversion_withNullLocation_returnsDTOWithNullLatLng() {
            // Arrange
            val dto = AddressGenerator.generateAddressDTO().toRequestDTO().copy(location = null)

            // Act
            val responseDTO = dto.toResponse()

            // Assert
            assertEquals(dto.id, responseDTO.id)
            assertEquals(dto.label, responseDTO.label)
            assertEquals(dto.address, responseDTO.address)
            assertEquals(dto.streetAddress, responseDTO.streetAddress)
            assertEquals(dto.city, responseDTO.city)
            assertEquals(dto.state, responseDTO.state)
            assertEquals(dto.pinCode, responseDTO.pinCode)
            assertEquals(dto.createdAt, responseDTO.createdAt)

            assertNull(responseDTO.latitude)
            assertNull(responseDTO.longitude)
        }

        @Test
        fun onConversion_withNonNullLocation_returnsDTOWithCorrectLocation() {
            // Arrange
            val genDTO = AddressGenerator.generateAddressDTO()
            val dto = genDTO.toRequestDTO()
            // Act
            val responseDTO = dto.toResponse()

            // Assert
            assertEquals(dto.id, responseDTO.id)
            assertEquals(dto.label, responseDTO.label)
            assertEquals(dto.address, responseDTO.address)
            assertEquals(dto.streetAddress, responseDTO.streetAddress)
            assertEquals(dto.city, responseDTO.city)
            assertEquals(dto.state, responseDTO.state)
            assertEquals(dto.pinCode, responseDTO.pinCode)
            assertEquals(dto.createdAt, responseDTO.createdAt)

            assertNotNull(responseDTO.latitude)
            assertEquals(genDTO.latitude, responseDTO.latitude)
            assertNotNull(responseDTO.longitude)
            assertEquals(genDTO.longitude, responseDTO.longitude)
        }
    }

    @Nested
    inner class LocalAddressToAddressRequestDTOTests {

        @Test
        fun onConversion_withNullLocation_returnsDTOWithNullLatLng() {
            // Arrange
            val localAddress =
                AddressGenerator.generateLocalAddress().copy(location = null)

            // Act
            val responseDTO = localAddress.toRequestDTO()

            // Assert
            assertEquals(localAddress.id, responseDTO.id)
            assertEquals(localAddress.label, responseDTO.label)
            assertEquals(localAddress.address?.address, responseDTO.address)
            assertEquals(localAddress.address?.streetAddress, responseDTO.streetAddress)
            assertEquals(localAddress.address?.city, responseDTO.city)
            assertEquals(localAddress.address?.state, responseDTO.state)
            assertEquals(localAddress.address?.pinCode, responseDTO.pinCode)
            assertNull(responseDTO.location)
        }

        @Test
        fun onConversion_withNonNullLocation_returnsDTOWithCorrectLocation() {
            // Arrange
            val localAddress =
                AddressGenerator.generateLocalAddress()

            // Act
            val responseDTO = localAddress.toRequestDTO()

            // Assert
            assertEquals(localAddress.id, responseDTO.id)
            assertEquals(localAddress.label, responseDTO.label)
            assertEquals(localAddress.address?.address, responseDTO.address)
            assertEquals(localAddress.address?.streetAddress, responseDTO.streetAddress)
            assertEquals(localAddress.address?.city, responseDTO.city)
            assertEquals(localAddress.address?.state, responseDTO.state)
            assertEquals(localAddress.address?.pinCode, responseDTO.pinCode)
            assertNotNull(responseDTO.location)
            assertEquals(
                "POINT(${localAddress.location?.longitude} ${localAddress.location?.latitude})",
                responseDTO.location
            )
        }
    }
}