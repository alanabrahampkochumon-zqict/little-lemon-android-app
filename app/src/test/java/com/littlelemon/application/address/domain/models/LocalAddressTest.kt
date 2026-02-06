package com.littlelemon.application.address.domain.models

import com.littlelemon.application.utils.AddressGenerator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows


class LocalAddressTest {

    private val physicalAddress = AddressGenerator.generatePhysicalAddress()
    private val localLocation = AddressGenerator.generateLocalLocation()

    @Test
    fun onLocalAddressInit_nullLocation_throwsNoException() {
        assertDoesNotThrow {
            LocalAddress(
                label = null,
                address = null,
                location = localLocation
            )
        }
    }

    @Test
    fun onLocalAddressInit_nullPhysicalAddress_throwsNoException() {
        assertDoesNotThrow {
            LocalAddress(
                label = null,
                address = null,
                location = localLocation
            )
        }
    }

    @Test
    fun onLocalAddressInit_nonNullAddressAndLocation_throwsNoException() {
        assertDoesNotThrow {
            LocalAddress(
                label = null,
                address = physicalAddress,
                location = localLocation
            )
        }
    }

    @Test
    fun onLocalAddressInit_nullAddressAndLocation_throwsNoException() {

        assertThrows<IllegalArgumentException> {
            LocalAddress(
                label = null,
                address = null,
                location = null
            )
        }
    }

}