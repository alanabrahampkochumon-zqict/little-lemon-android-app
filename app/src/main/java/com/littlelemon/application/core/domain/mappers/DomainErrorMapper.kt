package com.littlelemon.application.core.domain.mappers

import com.littlelemon.application.core.domain.utils.Error
import com.littlelemon.application.core.domain.utils.toNetworkError
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.ktor.client.plugins.HttpRequestTimeoutException

// NOTE: This mapper is coupled to supabase, modify when the internal implementation changes
internal fun Exception.mapToDomainError(): Error {
    return when (this) {
        is PostgrestRestException -> this.code?.toInt()?.toNetworkError()
            ?: Error.NetworkError.Unknown()

        is HttpRequestTimeoutException -> Error.NetworkError.Timeout()
        is HttpRequestException -> Error.NetworkError.Unknown()
        is IllegalStateException -> Error.Database
        else -> Error.Unknown
    }
}