package com.littlelemon.application.address.presentation.mappers

import com.littlelemon.application.address.domain.models.PhysicalAddress
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PhysicalAddressToFullAddressTest {
    private val buildingName = "Building name"
    private val streetAddress = "Street address"
    private val city = "City"
    private val state = "State"
    private val pinCode = "123456"

    private val expectedFullAddress = "$buildingName, $streetAddress, $city, $state $pinCode"

    @Test
    fun correctlyMapsAddress() {
        // Given a physical address
        val address = PhysicalAddress(
            address = buildingName,
            streetAddress = streetAddress,
            city = city,
            state = state,
            pinCode = pinCode
        )

        // When mapped to full address
        val fullAddress = address.toFullAddress()

        // Then the address produces the expected result
        assertEquals(expectedFullAddress, fullAddress)
    }

}