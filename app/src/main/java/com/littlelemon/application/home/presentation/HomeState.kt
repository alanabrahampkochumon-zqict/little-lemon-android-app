package com.littlelemon.application.home.presentation

import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.core.presentation.UiText


data class HomeState(
    val addresses: List<LocalAddress> = emptyList(),
    val addressLoading: Boolean = false,
    val addressError: UiText? = null,
)