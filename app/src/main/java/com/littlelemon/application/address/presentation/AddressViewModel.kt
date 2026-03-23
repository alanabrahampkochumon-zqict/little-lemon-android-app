package com.littlelemon.application.address.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlelemon.application.R
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.address.domain.usecase.GeocodeAddressUseCase
import com.littlelemon.application.address.domain.usecase.GetAddressUseCase
import com.littlelemon.application.address.domain.usecase.GetLocationUseCase
import com.littlelemon.application.address.domain.usecase.ReverseGeocodeLocationUseCase
import com.littlelemon.application.address.domain.usecase.SaveAddressUseCase
import com.littlelemon.application.address.presentation.AddressEvents.ShowError
import com.littlelemon.application.address.presentation.AddressEvents.ShowInfo
import com.littlelemon.application.address.presentation.mappers.toFullAddress
import com.littlelemon.application.address.presentation.mappers.toLocalAddress
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.core.domain.validators.RequiredFieldValidator
import com.littlelemon.application.core.presentation.UiText.DynamicString
import com.littlelemon.application.core.presentation.UiText.StringResource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// TODO: search for address and add an id
class AddressViewModel(
    private val getLocation: GetLocationUseCase,
    private val getAddress: GetAddressUseCase,
    private val geocodeAddress: GeocodeAddressUseCase,
    private val reverseGeocodedLocation: ReverseGeocodeLocationUseCase,
    private val saveAddress: SaveAddressUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddressState())
    val state = _state.asStateFlow()

    private val _addressChannel = Channel<AddressEvents>(Channel.BUFFERED)
    val addressEvents = _addressChannel.receiveAsFlow()


    fun onAction(action: AddressActions) {
        // Location Request and Permission
        when (action) {
            // State Updates
            is AddressActions.ChangeBuildingName -> _state.update {
                it.copy(
                    buildingName = action.address,
                    buildingNameError = null
                )
            }

            is AddressActions.ChangeCity -> _state.update {
                it.copy(
                    city = action.city,
                    cityError = null
                )
            }

            is AddressActions.ChangeLabel -> _state.update { it.copy(label = action.label) }
            is AddressActions.ChangePinCode -> _state.update {
                it.copy(
                    pinCode = action.pinCode,
                    pinCodeError = null
                )
            }

            is AddressActions.ChangeState -> _state.update {
                it.copy(
                    state = action.state,
                    stateError = null
                )
            }

            is AddressActions.ChangeStreetAddress -> _state.update {
                it.copy(
                    streetAddress = action.streetAddress,
                    streetAddressError = null
                )
            }

            is AddressActions.ChangeToDefaultAddress -> _state.update { it.copy(isDefaultAddress = action.value) }

            // Actions
            AddressActions.GetLocation -> viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                val events = mutableListOf<AddressEvents>()
                when (val result = getLocation()) {
                    is Resource.Failure -> {
                        _state.update { it.copy(isLoading = false) }
                        events.add(
                            ShowError(
                                if (result.errorMessage != null) DynamicString(
                                    result.errorMessage
                                )
                                else StringResource(R.string.generic_error_message)
                            )
                        )
                    }

                    is Resource.Loading -> Unit
                    is Resource.Success -> {
                        if (result.data != null) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    latitude = result.data.latitude,
                                    longitude = result.data.longitude
                                )
                            }
                        } else {
                            _state.update { it.copy(isLoading = false) }
                        }
                        events.add(ShowInfo(StringResource(R.string.location_granted_success_message)))
                        events.add(AddressEvents.LocationRetrievalSuccess)
                    }
                }

                for (evt in events)
                    _addressChannel.send(evt)
            }

            AddressActions.EnterLocationManually -> viewModelScope.launch {
                _addressChannel.send(AddressEvents.ShowLocationEntryPopup)
            }

            is AddressActions.SaveAddress -> viewModelScope.launch {
                if (!validateAddress()) return@launch
                var showDialog = true
                _state.update { it.copy(isLoading = true) }
                val events = mutableListOf<AddressEvents>()
                when (val result = saveAddress(state.value.toLocalAddress())) {
                    is Resource.Failure -> {
                        events.add(
                            ShowError(
                                if (result.errorMessage != null) DynamicString(
                                    result.errorMessage
                                ) else StringResource(R.string.generic_error_message)
                            )
                        )
                        showDialog = true
                    }

                    is Resource.Loading -> Unit // Already Handled
                    is Resource.Success -> {
                        events.add(
                            ShowInfo(StringResource(R.string.location_saved_success_message))
                        )
                        events.add(AddressEvents.AddressSaveSuccess)
                        showDialog = false
                    }
                }
                _state.update { it.copy(isLoading = false, showLocationDialog = showDialog) }
                for (evt in events) {
                    _addressChannel.send(evt)
                }
            }

            AddressActions.ShowLocationDialog -> _state.update {
                it.copy(
                    showLocationDialog = true
                )
            }

            AddressActions.DismissLocationDialog -> _state.update { it.copy(showLocationDialog = false) }

            is AddressActions.SaveLocation -> viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                when (val result = saveAddress(state.value.toLocalAddress())) {
                    is Resource.Failure -> {
                        val errorMessage = if (result.errorMessage != null) {
                            DynamicString(result.errorMessage)
                        } else {
                            StringResource(R.string.location_saving_unknown_error)
                        }
                        _state.update { it.copy(isLoading = false) }
                        _addressChannel.send(ShowError(errorMessage))
                    }

                    is Resource.Loading -> Unit
                    is Resource.Success -> {
                        _state.update { it.copy(isLoading = false) }
                        _addressChannel.send(AddressEvents.AddressSaveSuccess)
                    }
                }
            }

            AddressActions.GeocodeAddress -> viewModelScope.launch {
                if (state.value.buildingName.isBlank() && state.value.streetAddress.isBlank() && state.value.city.isBlank() && state.value.state.isBlank() && state.value.pinCode.isBlank())
                    return@launch
                _state.update { it.copy(isLoading = true) }
                val events = mutableListOf<AddressEvents>()
                when (val result = geocodeAddress(state.value.toFullAddress())) {
                    is Resource.Failure -> {
                        _state.update { it.copy(isLoading = false) }
                        val errorMessage =
                            if (result.errorMessage != null) DynamicString(result.errorMessage) else StringResource(
                                R.string.geocoding_error_message
                            )
                        events.add(ShowError(errorMessage))
                    }

                    is Resource.Loading -> Unit
                    is Resource.Success -> {
                        result.data?.let { (_, _, _, _, location) ->
                            _state.update {
                                it.copy(
                                    latitude = it.latitude ?: location?.latitude,
                                    longitude = it.longitude ?: location?.longitude,
                                    isLoading = false
                                )
                            }
                        }
                        if (result.data == null) {
                            _state.update { it.copy(isLoading = false) }
                        }
                        events.add(ShowInfo(StringResource(R.string.geocoding_success_message)))
                        events.add(AddressEvents.GeocodeSuccess)
                    }
                }
                for (evt in events)
                    _addressChannel.send(evt)
            }

            AddressActions.ReverseGeocodeLocation -> viewModelScope.launch {
                if (state.value.latitude == null || state.value.longitude == null)
                    return@launch _addressChannel.send(ShowError(StringResource(R.string.invalid_location_error)))
                _state.update { it.copy(isLoading = true) }
                val events = mutableListOf<AddressEvents>()
                when (val result = reverseGeocodedLocation(
                    location = LocalLocation(
                        state.value.latitude!!,
                        state.value.longitude!!
                    )
                )) {
                    is Resource.Failure -> {
                        _state.update { it.copy(isLoading = false) }
                        val errorMessage =
                            if (result.errorMessage != null) DynamicString(result.errorMessage) else StringResource(
                                R.string.reverse_geocoding_error_message
                            )
                        events.add(ShowError(errorMessage))
                    }

                    is Resource.Loading -> Unit
                    is Resource.Success -> {
                        result.data?.let { (_, _, _, address, _) ->
                            _state.update {
                                // TODO: Convert to mapper
                                it.copy(
                                    isLoading = false,
                                    buildingName = coalesceEmpty(
                                        it.buildingName,
                                        address?.address ?: ""
                                    ),
                                    streetAddress = coalesceEmpty(
                                        it.streetAddress,
                                        address?.streetAddress ?: ""
                                    ),
                                    city = coalesceEmpty(it.city, address?.city ?: ""),
                                    state = coalesceEmpty(it.state, address?.state ?: ""),
                                    pinCode = coalesceEmpty(it.pinCode, address?.pinCode ?: ""),
                                    stateError = null,
                                    streetAddressError = null,
                                    cityError = null,
                                )
                            }
                        }
                        events.add(ShowInfo(StringResource(R.string.reverse_geocoding_success_message)))
                        events.add(AddressEvents.ReverseGeocodeSuccess)
                        if (result.data == null) {
                            _state.update { it.copy(isLoading = false) }
                        }
                    }
                }
                for (evt in events)
                    _addressChannel.send(evt)
            }
        }
    }

    private fun coalesceEmpty(value: String, default: String): String {
        if (value.isBlank() || value.isEmpty())
            return default
        return value
    }

    // TODO: Refactor to map
    private fun validateAddress(): Boolean {
        val validate = RequiredFieldValidator()
        var isValid = true
        var newState = state.value.copy()
        if (!(validate(state.value.buildingName))) {
            newState =
                newState.copy(buildingNameError = StringResource(R.string.building_name_required_error))
            isValid = false
        }
        if (!(validate(state.value.streetAddress))) {
            newState =
                newState.copy(streetAddressError = StringResource(R.string.street_address_required_error))
            isValid = false
        }
        if (!(validate(state.value.city))) {
            newState = newState.copy(cityError = StringResource(R.string.city_required_error))
            isValid = false
        }
        if (!(validate(state.value.state))) {
            newState = newState.copy(stateError = StringResource(R.string.state_required_error))
            isValid = false
        }
        if (!(validate(state.value.pinCode))) {
            newState =
                newState.copy(pinCodeError = StringResource(R.string.pin_code_required_error))
            isValid = false
        }
        _state.update { newState }
        return isValid
    }
}
