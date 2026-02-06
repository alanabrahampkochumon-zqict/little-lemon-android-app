package com.littlelemon.application.address.domain.models

data class LocalAddress(
    val label: String? = null,
    val address: PhysicalAddress? = null,
    val location: LocalLocation? = null
) {
    init {
        require(address != null || location != null) {
            "Invalid address: Must contain a physical address(address, street, pincode, etc.) or a location(latlng)"
        }
    }
}
