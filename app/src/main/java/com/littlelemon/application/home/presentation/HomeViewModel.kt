package com.littlelemon.application.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlelemon.application.address.domain.usecase.GetAddressUseCase
import com.littlelemon.application.core.domain.utils.Resource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val getAddressUseCase: GetAddressUseCase
) : ViewModel() {
    val addresses =
        getAddressUseCase().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            Resource.Loading(null)
        )
}