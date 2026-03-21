package com.littlelemon.application.address.presentation.mappers

import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.address.domain.models.PhysicalAddress
import com.littlelemon.application.address.presentation.AddressState

fun AddressState.toLocalAddress(): LocalAddress {
    return LocalAddress(
        id = addressId,
        label = label,
        address = PhysicalAddress(
            address = buildingName,
            streetAddress = streetAddress,
            city = city,
            state = state,
            pinCode = pinCode
        ),
        location = if (latitude != null && longitude != null) LocalLocation(
            latitude = latitude,
            longitude = longitude
        ) else null,
        isDefault = isDefaultAddress
    )
}