package com.littlelemon.application.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlelemon.application.core.domain.AddressManager
import com.littlelemon.application.core.domain.SessionManager
import com.littlelemon.application.core.domain.model.SessionStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class RootViewModel(sessionManager: SessionManager, addressManager: AddressManager) : ViewModel() {

    val sessionStatus = sessionManager.getCurrentSessionStatus().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        SessionStatus.SessionLoading
    )

    val userHasAddress: StateFlow<Boolean?> = flow {
        emit(addressManager.userHasAddress())
    }.stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5_000), initialValue = null)

}