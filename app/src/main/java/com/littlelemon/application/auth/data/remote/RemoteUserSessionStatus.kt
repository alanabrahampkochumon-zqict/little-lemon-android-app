package com.littlelemon.application.auth.data.remote

sealed interface RemoteUserSessionStatus {
    data object Authenticated : RemoteUserSessionStatus
    data object Unauthenticated : RemoteUserSessionStatus
    data object Initializing : RemoteUserSessionStatus
}