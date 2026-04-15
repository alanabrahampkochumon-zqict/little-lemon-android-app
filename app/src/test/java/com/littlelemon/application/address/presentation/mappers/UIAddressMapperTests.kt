package com.littlelemon.application.address.presentation.mappers

import com.littlelemon.application.address.presentation.AddressState
import com.littlelemon.application.address.utils.AddressGenerator
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertNull
import kotlin.test.Test
import kotlin.test.assertEquals

class UIAddressMapperTests {


    @Nested
    inner class UIStateToLocalAddressMapper {

        @Test
        fun correctlyMapsAddress() {
            // Given a UI State
            val uiState = AddressGenerator.generateUIState()

            // When mapped to a local address
            val lLocation = uiState.toLocalAddress()

            // Then all the fields are correctly mapped
            assertEquals(uiState.addressId, lLocation.id)
            assertEquals(uiState.longitude, lLocation.location?.longitude)
            assertEquals(uiState.latitude, lLocation.location?.latitude)
            assertEquals(uiState.buildingName, lLocation.address?.address)
            assertEquals(uiState.streetAddress, lLocation.address?.streetAddress)
            assertEquals(uiState.state, lLocation.address?.state)
            assertEquals(uiState.city, lLocation.address?.city)
            assertEquals(uiState.pinCode, lLocation.address?.pinCode)
        }

        @Test
        fun nullLatLng_mapsToNullLocation() {
            // Given a UI State will null lat lng
            val uiState = AddressGenerator.generateUIState().copy(latitude = null, longitude = null)

            // When mapped to a local address
            val lLocation = uiState.toLocalAddress()

            // Then lat lng is null
            assertNull(lLocation.location)
        }

    }

    @Nested
    inner class UIStateToFullAddressTests {

        private val buildingName = "Building name"
        private val streetAddress = "Street address"
        private val city = "City"
        private val state = "State"
        private val pinCode = "123456"

        private val fullAddress = "$buildingName, $streetAddress, $city, $state $pinCode"
        private val noStatePinCodeAddress = "$buildingName, $streetAddress, $city"
        private val noBuildingNameAddress = "$streetAddress, $city, $state $pinCode"

        @Test
        fun correctlyMapsAddress() {
            // Given a UI state
            val uiState = AddressState(
                buildingName = buildingName,
                streetAddress = streetAddress,
                city = city,
                state = state,
                pinCode = pinCode
            )

            // When mapped to full address
            val address = uiState.toFullAddress()

            // Then the address produces the expected result
            assertEquals(fullAddress, address)
        }


        @Test
        fun emptyLastFields_leavesNotTrailingCommas() {
            // Given a UI state
            val uiState = AddressState(
                buildingName = buildingName,
                streetAddress = streetAddress,
                city = city,
            )

            // When mapped to full address
            val address = uiState.toFullAddress()

            // Then the address produces the expected result
            assertEquals(noStatePinCodeAddress, address)
        }

        @Test
        fun emptyFirstField_leavesNoLeadingComma() {
            // Given a UI state
            val uiState = AddressState(
                streetAddress = streetAddress,
                city = city,
                state = state,
                pinCode = pinCode
            )

            // When mapped to full address
            val address = uiState.toFullAddress()

            // Then the address produces the expected result
            assertEquals(noBuildingNameAddress, address)
        }
    }

}