package com.littlelemon.application.auth.domain.models

/**
 * Wrapper class for wrapping auth module's implementation of user session status.
 * This is intended as a wrapper for remote api's session status calls.
 */
sealed interface UserSessionStatus {

    data object Authenticated : UserSessionStatus
    data object Unauthenticated : UserSessionStatus
    data object Initializing : UserSessionStatus

}