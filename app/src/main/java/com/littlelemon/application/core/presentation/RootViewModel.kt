package com.littlelemon.application.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlelemon.application.core.domain.SessionManager
import com.littlelemon.application.core.domain.model.SessionStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class RootViewModel(private val sessionManager: SessionManager) : ViewModel() {

    val sessionStatus = sessionManager.getCurrentSessionStatus().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        SessionStatus.SessionLoading
    )

}