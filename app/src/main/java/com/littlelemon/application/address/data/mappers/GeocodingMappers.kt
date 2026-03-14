package com.littlelemon.application.address.data.mappers

import com.google.maps.errors.ApiException
import com.google.maps.errors.OverDailyLimitException
import com.google.maps.errors.OverQueryLimitException
import com.google.maps.errors.ZeroResultsException
import com.google.maps.model.AddressComponentType
import com.google.maps.model.GeocodingResult
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

fun GeocodingResult.toGeocodingDTO(): GeocodingDTO {
    val requiredComponents = mutableMapOf(
        AddressComponentType.STREET_ADDRESS to "", AddressComponentType.COUNTRY to "",
        AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1 to "", AddressComponentType.LOCALITY to "",
        AddressComponentType.POSTAL_CODE to ""
    )
    if (addressComponents != null)
        for (addressComponent in addressComponents) {
            for (type in addressComponent.types)
                if (type in requiredComponents.keys)
                    requiredComponents[type] = addressComponent.longName
        }

    val address = GeocodingDTO.Address(
        (formattedAddress ?: "").split(",").firstOrNull() ?: "",
        streetAddress = requiredComponents[AddressComponentType.STREET_ADDRESS],
        city = requiredComponents[AddressComponentType.LOCALITY],
        state = requiredComponents[AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1],
        country = requiredComponents[AddressComponentType.COUNTRY],
        pinCode = requiredComponents[AddressComponentType.POSTAL_CODE]
    )

    return GeocodingDTO(
        latLng = GeocodingDTO.LatLng(geometry.location.lat, geometry.location.lng),
        locationType = geometry.locationType.toLocationTypeDTO(),
        partialMatch = partialMatch,
        fullAddress = formattedAddress ?: "",
        address = address,
        placeId = placeId ?: ""
    )
}