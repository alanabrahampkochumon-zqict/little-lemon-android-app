package com.littlelemon.application.address.domain

/**
 * Exception wrapper for Google's Geocoding API
 * View https://developers.google.com/maps/documentation/geocoding/requests-geocoding#json for more details
 */
sealed interface GeocoderException
data class ZeroResultsException(val message: String? = null) : GeocoderException
data class DailyLimitException(val message: String? = null) : GeocoderException
data class QueryLimitException(val message: String? = null) : GeocoderException
data class RequestDeniedException(val message: String? = null) : GeocoderException
data class InvalidGeocodingRequestException(val message: String? = null) : GeocoderException
data class UnknownException(val message: String? = null) : GeocoderException