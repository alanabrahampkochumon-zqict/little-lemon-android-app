package com.littlelemon.application.address.domain.mappers

import com.littlelemon.application.address.domain.DEFAULT_ADDRESS_LABEL
import com.littlelemon.application.address.utils.AddressGenerator
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AddressMapperTests {

    @Test
    fun returnLocalAddressOnMapping() {
        // Given a geocoded address
        val geocodedAddress = AddressGenerator.generateGeocodedAddress()

        // When mapped to local address
        val localAddress = geocodedAddress.toLocalAddress()

        // Then, the property are a one-to-one map
        assertEquals(geocodedAddress.address, localAddress.address)
        assertEquals(geocodedAddress.id, localAddress.id)
        assertEquals(geocodedAddress.location, localAddress.location)
        assertEquals(DEFAULT_ADDRESS_LABEL, localAddress.label)
    }

}