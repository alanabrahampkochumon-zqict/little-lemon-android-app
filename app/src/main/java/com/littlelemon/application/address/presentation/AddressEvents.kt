package com.littlelemon.application.address.presentation

import com.littlelemon.application.core.presentation.UiText

sealed interface AddressEvents {
    data object ShowLocationEntryPopup : AddressEvents
    data object AddressSaved : AddressEvents
    data object LocationRetrieved : AddressEvents
    data class ShowError(val errorMessage: UiText) : AddressEvents
    data class ShowInfo(val message: UiText) : AddressEvents
}