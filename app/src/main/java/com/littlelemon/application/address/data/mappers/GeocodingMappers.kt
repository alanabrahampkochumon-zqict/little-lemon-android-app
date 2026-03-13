package com.littlelemon.application.address.data.mappers

import com.google.maps.errors.ApiException
import com.google.maps.errors.OverDailyLimitException
import com.google.maps.errors.OverQueryLimitException
import com.google.maps.errors.ZeroResultsException
import com.google.maps.model.LocationType
import com.google.maps.model.LocationType.APPROXIMATE
import com.google.maps.model.LocationType.GEOMETRIC_CENTER
import com.google.maps.model.LocationType.RANGE_INTERPOLATED
import com.google.maps.model.LocationType.ROOFTOP
import com.google.maps.model.LocationType.UNKNOWN
import com.littlelemon.application.address.data.remote.models.GeocodingDTO
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

fun LocationType.toLocationTypeDTO(): GeocodingDTO.LocationType {
    return when (this) {
        ROOFTOP -> GeocodingDTO.LocationType.ROOFTOP
        RANGE_INTERPOLATED -> GeocodingDTO.LocationType.RANGE_INTERPOLATED
        GEOMETRIC_CENTER -> GeocodingDTO.LocationType.GEOMETRIC_CENTER
        APPROXIMATE -> GeocodingDTO.LocationType.APPROXIMATE
        UNKNOWN -> GeocodingDTO.LocationType.UNKNOWN
    }
}