package com.littlelemon.application.profile.presentation

import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.core.presentation.UiText

data class ProfileState(
    val address: List<LocalAddress> = emptyList(),
    val addressLoading: Boolean = false,
    val addressError: UiText? = null,

    val userName: String = "",
    val email: String = ""
)
