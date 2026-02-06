package com.littlelemon.application.utils

import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.address.domain.models.PhysicalAddress
import io.github.serpro69.kfaker.faker
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object AddressGenerator {

    private val faker = faker { }
    private const val LATITUDE_LIMIT = 180

    private const val LONGITUDE_LIMIT = 360

    @OptIn(ExperimentalUuidApi::class)
    fun generateLocalAddress(): LocalAddress {
        return LocalAddress(
            label = Uuid.random().toString(),
            address = generatePhysicalAddress(),
            location = generateLocalLocation()
        )
    }

    fun generatePhysicalAddress(): PhysicalAddress {
        return PhysicalAddress(
            faker.address.secondaryAddress(),
            faker.address.streetAddress(),
            faker.address.state(),
            faker.address.state(),
            faker.address.postcode()
        )
    }

    fun generateLocalLocation(): LocalLocation {
        // 0 to 180 - (180 / 2) => -90 to 90
        // 0 to 360 - (360 / 2) => -180 to 180
        return LocalLocation(
            Math.random() * LATITUDE_LIMIT - (LATITUDE_LIMIT / 2),
            Math.random() * LONGITUDE_LIMIT - (LONGITUDE_LIMIT / 2)
        )
    }
}