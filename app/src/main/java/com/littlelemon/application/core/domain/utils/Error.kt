package com.littlelemon.application.core.domain.utils

sealed interface Error {
    sealed class NetworkError(val code: Int) : Error {
        class BadRequest : NetworkError(400)
        class Unauthorized : NetworkError(401)
        class Forbidden : NetworkError(403)
        class NotFound : NetworkError(404)
        class Unknown : NetworkError(0) // General Network Errors
    }

    data object PermissionDenied : Error

    sealed interface SessionError : Error {
        data object InvalidToken : SessionError
        data object SessionTokenNotFound : SessionError
        data object TokenRefreshFailed : SessionError
        data object Unknown : SessionError
    }
}
