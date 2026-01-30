package com.littlelemon.application.core.domain.utils

import com.littlelemon.application.core.domain.utils.Error.NetworkError
import io.ktor.http.HttpStatusCode
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

fun HttpStatusCode.toNetworkError(): NetworkError? {
    if (this.value < 350) return null
    return when (this) {
        HttpStatusCode.BadRequest -> NetworkError.BadRequest()
        HttpStatusCode.Unauthorized -> NetworkError.Unauthorized()
        HttpStatusCode.Forbidden -> NetworkError.Forbidden()
        HttpStatusCode.NotFound -> NetworkError.NotFound()
        else -> NetworkError.Unknown()
    }
}

fun Int.toNetworkError(): NetworkError? {
    if (this < 350) return null
    return when (this) {
        400 -> NetworkError.BadRequest()
        401 -> NetworkError.Unauthorized()
        403 -> NetworkError.Forbidden()
        404 -> NetworkError.NotFound()
        else -> NetworkError.Unknown()
    }
}

fun Long.toLocalDateTime(timezone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime {
    val instant = Instant.fromEpochMilliseconds(this)
    return instant.toLocalDateTime(timezone)
}