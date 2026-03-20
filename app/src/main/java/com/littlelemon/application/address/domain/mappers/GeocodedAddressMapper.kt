package com.littlelemon.application.address.domain.mappers

import com.littlelemon.application.address.domain.DEFAULT_ADDRESS_LABEL
import com.littlelemon.application.address.domain.models.GeocodedAddress
import com.littlelemon.application.address.domain.models.LocalAddress

fun GeocodedAddress.toLocalAddress(): LocalAddress {
    return LocalAddress(
        address = address,
        id = this.id,
        label = DEFAULT_ADDRESS_LABEL,
        location = this.location,
        isDefault = false,
    )
}