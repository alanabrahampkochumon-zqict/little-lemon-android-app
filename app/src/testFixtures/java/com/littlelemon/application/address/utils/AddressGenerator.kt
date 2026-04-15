package com.littlelemon.application.address.utils

import com.littlelemon.application.address.data.local.models.AddressEntity
import com.littlelemon.application.address.data.remote.models.AddressDTO
import com.littlelemon.application.address.domain.models.GeocodedAddress
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.address.domain.models.PhysicalAddress
import com.littlelemon.application.address.presentation.AddressState
import io.github.serpro69.kfaker.faker
import kotlin.math.roundToLong
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
object AddressGenerator {

    private val faker = faker { }
    private const val LATITUDE_LIMIT = 180

    private const val LONGITUDE_LIMIT = 360


    fun generateLocalAddress(): LocalAddress {
        return LocalAddress(
            id = Uuid.Companion.random().toString(),
            label = Uuid.Companion.random().toString(),
            address = generatePhysicalAddress(),
            location = generateLocalLocation(),
            isDefault = Math.random() > 0.5
        )
    }

    fun generateGeocodedAddress(): GeocodedAddress {
        return GeocodedAddress(
            id = Uuid.Companion.random().toString(),
            partialMatch = Math.random() > 0.5,
            fullAddress = faker.address.fullAddress(),
            address = generatePhysicalAddress(),
            location = generateLocalLocation()
        )
    }

    fun generateUIState(): AddressState {
        return AddressState(
            addressId = Uuid.Companion.random().toString(),
            label = faker.name.name(),
            buildingName = faker.address.secondaryAddress(),
            streetAddress = faker.address.streetAddress(),
            city = faker.address.city(),
            state = faker.address.state(),
            pinCode = faker.address.postcode(),
            latitude = Math.random() * LATITUDE_LIMIT - (LATITUDE_LIMIT / 2),
            longitude = Math.random() * LONGITUDE_LIMIT - (LONGITUDE_LIMIT / 2),
        )
    }

    fun generateAddressDTO(): AddressDTO {
        return AddressDTO(
            id = Uuid.Companion.random().toString(),
            label = faker.name.name(),
            address = faker.address.secondaryAddress(),
            streetAddress = faker.address.streetAddress(),
            city = faker.address.city(),
            state = faker.address.state(),
            pinCode = faker.address.postcode(),
            latitude = Math.random() * LATITUDE_LIMIT - (LATITUDE_LIMIT / 2),
            longitude = Math.random() * LONGITUDE_LIMIT - (LONGITUDE_LIMIT / 2),
            createdAt = Instant.Companion.fromEpochMilliseconds((Math.random() * 1000000).roundToLong()),
            isDefault = Math.random() > 0.5
        )
    }

    fun generateAddressEntity(): AddressEntity {
        return AddressEntity(
            id = Uuid.Companion.random().toString(),
            label = faker.name.name(),
            address = faker.address.secondaryAddress(),
            streetAddress = faker.address.streetAddress(),
            city = faker.address.city(),
            state = faker.address.state(),
            pinCode = faker.address.postcode(),
            latitude = Math.random() * LATITUDE_LIMIT - (LATITUDE_LIMIT / 2),
            longitude = Math.random() * LONGITUDE_LIMIT - (LONGITUDE_LIMIT / 2),
            createdAt = Instant.Companion.fromEpochMilliseconds((Math.random() * 1000000).roundToLong()),
            isDefault = Math.random() > 0.5
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