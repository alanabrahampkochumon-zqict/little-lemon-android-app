package com.littlelemon.application.address.data.mappers

import android.location.Location
import com.littlelemon.application.address.data.local.models.AddressEntity
import com.littlelemon.application.address.data.remote.models.AddressDTO
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.address.domain.models.PhysicalAddress

fun Location.toLocalLocation(): LocalLocation = LocalLocation(this.latitude, this.longitude)

fun AddressEntity.toLocalAddress(): LocalAddress = LocalAddress(
    label = label,
    address = if (address != null && city != null && pinCode != null) PhysicalAddress(
        address = address,
        streetAddress = streetAddress ?: "",
        city = city,
        state = state ?: "",
        pinCode = pinCode
    ) else null,
    location = if (this.latitude != null && this.longitude != null) LocalLocation(
        this.latitude,
        this.longitude
    ) else null
)

fun AddressDTO.toAddressEntity(): AddressEntity = AddressEntity(
    id = id,
    label = label,
    address = address,
    streetAddress = streetAddress,
    city = city,
    state = state,
    pinCode = pinCode,
    latitude = latitude,
    longitude = longitude,
    createdAt = createdAt,
)