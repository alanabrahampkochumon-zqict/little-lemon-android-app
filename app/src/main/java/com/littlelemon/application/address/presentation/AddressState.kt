package com.littlelemon.application.address.presentation

import com.littlelemon.application.core.presentation.UiText

data class AddressState(
    // General
    val isLoading: Boolean = false,
    val showLocationDialog: Boolean = false,

    // Location
    val label: String = "",
    val address: String = "",
    val addressError: UiText? = null,
    val streetAddress: String = "",
    val streetAddressError: UiText? = null,
    val city: String = "",
    val cityError: UiText? = null,
    val state: String = "",
    val stateError: UiText? = null,
    val pinCode: String = "",
    val pinCodeError: UiText? = null,

    val latitude: Double? = null,
    val longitude: Double? = null
)