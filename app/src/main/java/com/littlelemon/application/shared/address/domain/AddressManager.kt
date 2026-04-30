package com.littlelemon.application.shared.address.domain

/**
 * Interface that manages address related function for the core module.
 * Must be implemented in the concerned module.
 * See [com.littlelemon.application.address.domain.DefaultAddressManager].
 */
interface AddressManager {
    suspend fun userHasAddress(): Boolean
}