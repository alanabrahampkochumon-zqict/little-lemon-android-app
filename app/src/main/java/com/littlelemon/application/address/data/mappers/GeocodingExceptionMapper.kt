package com.littlelemon.application.address.data.mappers

import com.google.maps.errors.ApiException
import com.google.maps.errors.OverDailyLimitException
import com.google.maps.errors.OverQueryLimitException
import com.google.maps.errors.ZeroResultsException
import com.littlelemon.application.address.domain.GeocoderException
import com.littlelemon.application.core.domain.exceptions.CoreException
import com.littlelemon.application.core.domain.exceptions.InvalidRequestException
import com.littlelemon.application.core.domain.exceptions.RequestDeniedException
import com.littlelemon.application.core.domain.exceptions.UnknownException
import com.google.maps.errors.InvalidRequestException as GeoApiInvalidRequestException
import com.google.maps.errors.RequestDeniedException as GeoApiRequestDeniedException

/**
 * Maps from Google API client exception to {@link CoreException}
 */
fun ApiException.toGeocodingException(): CoreException {
    return when (this) {
        is OverDailyLimitException -> GeocoderException.DailyLimit(this.message)
        is OverQueryLimitException -> GeocoderException.QueryLimit(this.message)
        is ZeroResultsException -> GeocoderException.ZeroResults(this.message)
        is GeoApiInvalidRequestException -> InvalidRequestException(this.message)
        is GeoApiRequestDeniedException -> RequestDeniedException(this.message)
        else -> UnknownException(this.message)
    }
}