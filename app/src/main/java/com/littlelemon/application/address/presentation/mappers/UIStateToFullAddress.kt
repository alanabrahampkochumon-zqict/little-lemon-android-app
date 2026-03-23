package com.littlelemon.application.address.presentation.mappers

import com.littlelemon.application.address.presentation.AddressState

fun AddressState.toFullAddress(): String {
    var address = ""
    address += buildingName.ifBlank { "" }
    address += if (buildingName.isNotBlank() && streetAddress.isNotBlank()) ", " else ""
    address += streetAddress.ifBlank { "" }
    address += if (streetAddress.isNotBlank() && city.isNotBlank()) ", " else ""
    address += city.ifBlank { "" }
    address += if (city.isNotBlank() && state.isNotBlank()) ", " else ""
    address += state.ifBlank { "" }
    address += if (state.isNotBlank() && pinCode.isNotBlank()) " " else ""
    address += pinCode.ifBlank { "" }
    return address
}