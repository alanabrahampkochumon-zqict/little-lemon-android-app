package com.littlelemon.application.address.presentation

sealed interface AddressActions {

    data class ChangeLabel(val label: String) : AddressActions
    data class ChangeAddress(val address: String) : AddressActions
    data class ChangeStreetAddress(val streetAddress: String) : AddressActions
    data class ChangeCity(val city: String) : AddressActions
    data class ChangeState(val state: String) : AddressActions
    data class ChangePinCode(val pinCode: String) : AddressActions
    data object RequestLocation : AddressActions
    data object EnterLocationManually : AddressActions
    data class SaveAddress(
        val label: String?,
        val address: String,
        val streetAddress: String,
        val city: String,
        val state: String,
        val pinCode: String,
        val latitude: Double? = null,
        val longitude: Double? = null
    ) : AddressActions // For Saving manual location //TODO: Modify to use LocalAddress

    data object AddressSaved : AddressActions
}