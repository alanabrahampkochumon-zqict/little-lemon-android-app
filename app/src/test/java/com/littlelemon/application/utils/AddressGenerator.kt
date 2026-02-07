package com.littlelemon.application.utils

import com.littlelemon.application.address.data.local.models.AddressEntity
import com.littlelemon.application.address.data.remote.models.AddressDTO
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.address.domain.models.PhysicalAddress
import io.github.serpro69.kfaker.faker
import kotlin.math.roundToLong
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

    @OptIn(ExperimentalUuidApi::class)
    fun generateAddressDTO(): AddressDTO {
        return AddressDTO(
            id = Uuid.random().toString(),
            label = faker.name.name(),
            address = faker.address.secondaryAddress(),
            streetAddress = faker.address.streetAddress(),
            city = faker.address.city(),
            state = faker.address.state(),
            pinCode = faker.address.postcode(),
            latitude = Math.random() * LATITUDE_LIMIT - (LATITUDE_LIMIT / 2),
            longitude = Math.random() * LONGITUDE_LIMIT - (LONGITUDE_LIMIT / 2),
            createdAt = (Math.random() * 1000000).roundToLong()
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    fun generateAddressEntity(): AddressEntity {
        return AddressEntity(
            id = Uuid.random().toString(),
            label = faker.name.name(),
            address = faker.address.secondaryAddress(),
            streetAddress = faker.address.streetAddress(),
            city = faker.address.city(),
            state = faker.address.state(),
            pinCode = faker.address.postcode(),
            latitude = Math.random() * LATITUDE_LIMIT - (LATITUDE_LIMIT / 2),
            longitude = Math.random() * LONGITUDE_LIMIT - (LONGITUDE_LIMIT / 2),
            createdAt = (Math.random() * 1000000).roundToLong()
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