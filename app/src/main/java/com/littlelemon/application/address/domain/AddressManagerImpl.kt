package com.littlelemon.application.address.domain

import com.littlelemon.application.address.domain.usecase.GetAddressCountUseCase
import com.littlelemon.application.core.domain.AddressManager

class AddressManagerImpl(private val getAddressCount: GetAddressCountUseCase) : AddressManager {
    override suspend fun userHasAddress(): Boolean {
        return getAddressCount() > 0
    }
}