package com.littlelemon.application.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlelemon.application.R
import com.littlelemon.application.address.domain.usecase.GetAddressUseCase
import com.littlelemon.application.address.domain.usecase.RemoveAddressUseCase
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.core.presentation.UiText
import com.littlelemon.application.profile.ProfileActions
import com.littlelemon.application.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    profileRepository: ProfileRepository,
    getAddresses: GetAddressUseCase,
    private val removeAddress: RemoveAddressUseCase
) :
    ViewModel() {


    // TODO: Add test
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    val addresses = getAddresses().map { resource ->
        when (resource) {
            is Resource.Failure -> ProfileAddressState(
                addressError = UiText.StringResource(R.string.address_loading_error_message),
                addressLoading = false
            )

            is Resource.Loading -> ProfileAddressState(addressLoading = true)
            is Resource.Success -> ProfileAddressState(
                addressLoading = false,
                address = resource.data ?: emptyList()
            )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ProfileAddressState(addressLoading = true)
    )

    init {
        viewModelScope.launch {
            val profile = profileRepository.getUserProfile()
            _state.update { it.copy(userName = profile.name, email = profile.email) }
        }
    }


    fun onAction(action: ProfileActions) {
        when (action) {
            is ProfileActions.RemoveAddress -> viewModelScope.launch { removeAddress(action.address) }
        }
    }


}