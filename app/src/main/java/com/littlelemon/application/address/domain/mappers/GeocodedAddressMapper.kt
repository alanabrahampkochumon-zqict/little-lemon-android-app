package com.littlelemon.application.address.domain.mappers

import com.littlelemon.application.address.domain.models.GeocodedAddress
import com.littlelemon.application.address.domain.models.LocalAddress

fun GeocodedAddress.toPhysicalAddress(): LocalAddress {
    return LocalAddress(
        address = address,
        id = this.id,
        label = "Unnamed Address",
        location = this.location,
        isDefault = false,
    )
}