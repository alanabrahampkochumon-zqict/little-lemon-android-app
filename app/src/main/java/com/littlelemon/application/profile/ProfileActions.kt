package com.littlelemon.application.profile

import com.littlelemon.application.address.domain.models.LocalAddress

sealed interface ProfileActions {

    data class RemoveAddress(val address: LocalAddress) : ProfileActions
}