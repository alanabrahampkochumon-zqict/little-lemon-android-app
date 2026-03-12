package com.littlelemon.application.address.domain

import com.littlelemon.application.core.domain.exceptions.CoreException

/**
 * Exception wrapper for Google's Geocoding API
 * View https://developers.google.com/maps/documentation/geocoding/requests-geocoding#json for more details
 */
sealed class GeocoderException(message: String?) : CoreException(message) {
    class ZeroResults(message: String? = null) : GeocoderException(message)
    class DailyLimit(message: String? = null) : GeocoderException(message)
    class QueryLimit(message: String? = null) : GeocoderException(message)
}
