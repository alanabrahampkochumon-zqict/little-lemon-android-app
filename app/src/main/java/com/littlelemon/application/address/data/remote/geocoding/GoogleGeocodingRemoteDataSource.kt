package com.littlelemon.application.address.data.remote.geocoding

import com.google.maps.errors.ApiException
import com.littlelemon.application.address.data.mappers.toGeocodingDTO
import com.littlelemon.application.address.data.mappers.toGeocodingException
import com.littlelemon.application.address.data.remote.models.GeocodingDTO
import com.littlelemon.application.address.domain.GeocoderException
import com.littlelemon.application.core.domain.exceptions.InvalidRequestException
import com.littlelemon.application.core.domain.exceptions.RequestDeniedException
import com.littlelemon.application.core.domain.exceptions.UnknownException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

class GoogleGeocodingRemoteDataSource(
    private val engine: GeocodingEngine
) : GeocodingRemoteDataSource {
    @Throws(
        GeocoderException.ZeroResults::class,
        GeocoderException.DailyLimit::class,
        GeocoderException.QueryLimit::class,
        RequestDeniedException::class,
        InvalidRequestException::class,
        UnknownException::class,
    )
    override suspend fun geocodeAddress(address: String): GeocodingDTO {
        return try {
            val result = engine.geocode(address)
            if (result.isEmpty())
                throw GeocoderException.ZeroResults("No matching results found for $address")
            result.first().toGeocodingDTO()
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
}