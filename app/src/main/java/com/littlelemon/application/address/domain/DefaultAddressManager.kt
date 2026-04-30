package com.littlelemon.application.address.domain

import android.util.Log
import com.littlelemon.application.address.domain.usecase.GetAddressCountUseCase
import com.littlelemon.application.shared.address.domain.AddressManager

class DefaultAddressManager(private val getAddressCount: GetAddressCountUseCase) : AddressManager {
    override suspend fun userHasAddress(): Boolean {
        Log.d("Address Count", getAddressCount().toString())
        return getAddressCount() > 0
    }
}