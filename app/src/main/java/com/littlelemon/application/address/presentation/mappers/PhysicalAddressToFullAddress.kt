package com.littlelemon.application.address.presentation.mappers

import com.littlelemon.application.address.domain.models.PhysicalAddress

fun PhysicalAddress.toFullAddress(): String {
    var address = ""
    address += this.address.ifBlank { "" }
    address += if (this.address.isNotBlank() && streetAddress.isNotBlank()) ", " else ""
    address += streetAddress.ifBlank { "" }
    address += if (streetAddress.isNotBlank() && city.isNotBlank()) ", " else ""
    address += city.ifBlank { "" }
    address += if (city.isNotBlank() && state.isNotBlank()) ", " else ""
    address += state.ifBlank { "" }
    address += if (state.isNotBlank() && pinCode.isNotBlank()) " " else ""
    address += pinCode.ifBlank { "" }
    return address
}