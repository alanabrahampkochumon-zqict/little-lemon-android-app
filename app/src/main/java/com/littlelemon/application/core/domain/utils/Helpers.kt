package com.littlelemon.application.core.domain.utils

import com.littlelemon.application.core.domain.utils.Error.NetworkError
import io.ktor.http.HttpStatusCode

fun HttpStatusCode.toNetworkError(): NetworkError? {
    if (this.value < 350) return null
    return when(this) {
        HttpStatusCode.BadRequest -> NetworkError.BadRequest()
        HttpStatusCode.Unauthorized -> NetworkError.Unauthorized()
        HttpStatusCode.Forbidden -> NetworkError.Forbidden()
        HttpStatusCode.NotFound -> NetworkError.NotFound()
        else -> NetworkError.Unknown()
    }
}

fun Int.toNetworkError(): NetworkError? {
    if (this < 350) return null
    return when(this) {
        400 -> NetworkError.BadRequest()
        401 -> NetworkError.Unauthorized()
        403 -> NetworkError.Forbidden()
        404 -> NetworkError.NotFound()
        else -> NetworkError.Unknown()
    }
}