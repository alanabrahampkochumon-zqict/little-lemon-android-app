package com.littlelemon.application.address.presentation

sealed interface AddressActions {

    data class ChangeLabel(val label: String) : AddressActions
    data class ChangeAddress(val address: String) : AddressActions
    data class ChangeStreetAddress(val streetAddress: String) : AddressActions
    data class ChangeCity(val city: String) : AddressActions
    data class ChangeState(val state: String) : AddressActions
    data class ChangePinCode(val pinCode: String) : AddressActions
    data class ChangeToDefaultAddress(val value: Boolean) : AddressActions
    data object GetLocation : AddressActions
    data object EnterLocationManually : AddressActions


    data object ShowLocationDialog : AddressActions
    data object DismissLocationDialog : AddressActions
    data object SaveAddress :
        AddressActions // For Saving manual location //TODO: Modify to use LocalAddress

}