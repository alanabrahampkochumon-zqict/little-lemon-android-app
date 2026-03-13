package com.littlelemon.application.core.domain.utils

import com.google.maps.errors.ApiException
import com.littlelemon.application.address.data.mappers.toGeocodingException
import com.littlelemon.application.core.domain.exceptions.InvalidRequestException
import com.littlelemon.application.core.domain.exceptions.RequestDeniedException
import com.littlelemon.application.core.domain.exceptions.UnknownException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

suspend fun <T> safeApiCall(block: suspend () -> T): T {
    return try {
        block()
    } catch (e: ApiException) {
        throw e.toGeocodingException()
    } catch (e: IllegalStateException) {
        throw RequestDeniedException(e.message)
    } catch (e: IllegalArgumentException) {
        throw InvalidRequestException(e.message)
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        throw UnknownException(e.message)
    }
}