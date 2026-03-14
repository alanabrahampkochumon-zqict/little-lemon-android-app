package com.littlelemon.application.address.utils

import com.google.maps.model.AddressComponent
import com.google.maps.model.AddressComponentType
import com.google.maps.model.GeocodingResult
import com.google.maps.model.Geometry
import com.google.maps.model.LatLng
import com.google.maps.model.LocationType
import com.littlelemon.application.address.data.mappers.toLocationTypeDTO
import com.littlelemon.application.address.data.remote.models.GeocodingDTO
import io.github.serpro69.kfaker.faker
import java.util.UUID
import kotlin.random.Random

object GeocodingResultGenerator {
    val faker = faker { }

    fun generateResult(
        makeNull: AddressComponentType? = null,
        makeFullAddressEmpty: Boolean = false
    ): Pair<GeocodingResult, GeocodingDTO> {

        val addressMap = mutableMapOf(
            AddressComponentType.STREET_ADDRESS to faker.address.streetAddress(),
            AddressComponentType.COUNTRY to faker.address.country(),
            AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1 to faker.address.state(),
            AddressComponentType.LOCALITY to faker.address.city(),
            AddressComponentType.POSTAL_CODE to faker.address.postcode()
        )

        makeNull?.let { key ->
            addressMap[key] = ""
        }
        val placeId = UUID.randomUUID().toString()
        val partialMatch = Random.nextInt(0, 10) > 5
        val address = faker.address.streetAddress()
        val fullAddress =
            if (makeFullAddressEmpty) "" else "$address, ${faker.address.fullAddress()}"
        val geometry = Geometry()
        geometry.location = LatLng(Random.nextDouble(0.0, 180.0), Random.nextDouble(0.0, 180.0))
        geometry.locationType = LocationType.entries.random()

        val addressComponents = mutableListOf<AddressComponent>()
        for ((component, value) in addressMap) {
            if (value.isNotBlank()) {
                val addressComponent = AddressComponent()
                addressComponent.longName = value
                addressComponent.shortName = value
                addressComponent.types = arrayOf(component)
                addressComponents.add(addressComponent)
            }
        }


        val geocodingResult = GeocodingResult()
        geocodingResult.addressComponents = addressComponents.toTypedArray()
        geocodingResult.formattedAddress = fullAddress
        geocodingResult.geometry = geometry
        geocodingResult.placeId = placeId
        geocodingResult.partialMatch = partialMatch
        
        val geocodingDTO = GeocodingDTO(
            latLng = GeocodingDTO.LatLng(
                geometry.location.lat,
                geometry.location.lng
            ),
            locationType = geometry.locationType.toLocationTypeDTO(),
            fullAddress = fullAddress,
            partialMatch = partialMatch,
            address = GeocodingDTO.Address(
                address = address,
                streetAddress = addressMap[AddressComponentType.STREET_ADDRESS],
                city = addressMap[AddressComponentType.LOCALITY],
                state = addressMap[AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1],
                country = addressMap[AddressComponentType.COUNTRY],
                pinCode = addressMap[AddressComponentType.POSTAL_CODE]
            ),
            placeId = placeId
        )
        return geocodingResult to geocodingDTO
    }
}