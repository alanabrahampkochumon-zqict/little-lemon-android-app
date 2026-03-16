package com.littlelemon.application.address.data.local.dao

import com.littlelemon.application.address.data.local.models.GeocodingEntity
import com.littlelemon.application.address.utils.GeocodingGenerator

class FakeGeocodeDao(
    entries: List<GeocodingEntity> = emptyList(),
    private val throwError: Boolean = false
) : GeocodingDao {

    private var geocodedAddressList = mutableListOf<GeocodingEntity>()
    private val defaultEntryCount = 50

    init {
        if (entries.isEmpty()) {
            repeat(defaultEntryCount) {
                geocodedAddressList.add(GeocodingGenerator.generateGeocodingEntity().first)
            }
        }
    }

    override suspend fun upsert(geocodingEntity: GeocodingEntity) {
        geocodedAddressList =
            geocodedAddressList.filter { entity -> entity.placeId == geocodingEntity.placeId }
                .toMutableList()
        geocodedAddressList.add(geocodingEntity)
    }

    override suspend fun getAddress(
        lat: Double,
        lng: Double
    ): GeocodingEntity? {
        return geocodedAddressList.find { (_, latLng) -> latLng.lat == lat && latLng.lng == lng }
    }

    override suspend fun getAddress(address: String): GeocodingEntity? {
        return geocodedAddressList.find { (_, _, _, _, fullAddress) -> fullAddress == address }
    }

    override suspend fun clearAll() {
        geocodedAddressList.clear()
    }

    override suspend fun getCount(): Int = geocodedAddressList.size

    override suspend fun clearExpired(timestamp: Long) {
        geocodedAddressList =
            geocodedAddressList.filter { entity -> entity.createdTimestamp > timestamp }
                .toMutableList()
    }


}