package com.littlelemon.application.address.presentation

import com.littlelemon.application.core.presentation.UiText

sealed interface AddressEvents {
    data object ShowLocationEntryPopup : AddressEvents
    data object AddressSaveSuccess : AddressEvents
    data object LocationRetrievalSuccess : AddressEvents
    data object GeocodeSuccess : AddressEvents
    data object ReverseGeocodeSuccess : AddressEvents
    data class ShowError(val errorMessage: UiText) : AddressEvents
    data class ShowInfo(val message: UiText) : AddressEvents
}