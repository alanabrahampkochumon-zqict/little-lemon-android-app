package com.littlelemon.application.address.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlelemon.application.R
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.address.domain.models.PhysicalAddress
import com.littlelemon.application.address.domain.usecase.GetAddressUseCase
import com.littlelemon.application.address.domain.usecase.GetLocationUseCase
import com.littlelemon.application.address.domain.usecase.SaveAddressUseCase
import com.littlelemon.application.address.presentation.AddressEvents.ShowError
import com.littlelemon.application.address.presentation.AddressEvents.ShowInfo
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.core.presentation.UiText.DynamicString
import com.littlelemon.application.core.presentation.UiText.StringResource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddressViewModel(
    private val getLocation: GetLocationUseCase,
    private val getAddress: GetAddressUseCase,
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
            is AddressActions.ChangeAddress -> _state.update {
                it.copy(
                    buildingName = action.address,
                    addressError = null
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

            // Actions
            AddressActions.GetLocation -> viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                val events = mutableListOf<AddressEvents>()

                when (val result = getLocation()) {
                    is Resource.Failure -> {
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
                        result.data?.let { data ->
                            _state.update {
                                it.copy(
                                    latitude = data.latitude,
                                    longitude = data.longitude
                                )
                            }
                        }
                        events.add(ShowInfo(StringResource(R.string.location_granted_success_message)))
                        events.add(AddressEvents.AddressSaved)
                    }
                }

                for (evt in events)
                    _addressChannel.send(evt)
                _state.update { it.copy(isLoading = false) }
            }

            AddressActions.EnterLocationManually -> viewModelScope.launch {
                _addressChannel.send(AddressEvents.ShowLocationEntryPopup)
            }

            is AddressActions.SaveAddress -> viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                val address = LocalAddress(
                    label = state.value.label,
                    address = PhysicalAddress(
                        address = state.value.buildingName,
                        streetAddress = state.value.streetAddress,
                        city = state.value.city,
                        state = state.value.state,
                        pinCode = state.value.pinCode
                    ),
                    location = if (state.value.latitude != null && state.value.longitude != null) LocalLocation(
                        latitude = state.value.latitude!!,
                        longitude = state.value.longitude!!
                    ) else null
                )
                val events = mutableListOf<AddressEvents>()
                when (val result = saveAddress(address)) {
                    is Resource.Failure -> {
                        events.add(
                            ShowError(
                                if (result.errorMessage != null) DynamicString(
                                    result.errorMessage
                                ) else StringResource(R.string.generic_error_message)
                            )
                        )
                    }

                    is Resource.Loading -> Unit // Already Handled
                    is Resource.Success -> {
                        events.add(
                            ShowInfo(StringResource(R.string.location_saved_success_message))
                        )
                        events.add(AddressEvents.AddressSaved)
                    }
                }
                _state.update { it.copy(isLoading = false) }
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
        }
    }

}