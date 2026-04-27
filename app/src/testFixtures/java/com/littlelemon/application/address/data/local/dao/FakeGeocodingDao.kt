package com.littlelemon.application.address.data.local.dao

import com.littlelemon.application.address.utils.GeocodingGenerator
import com.littlelemon.application.database.address.dao.GeocodingDao
import com.littlelemon.application.database.address.models.GeocodingEntity

class FakeGeocodingDao(
    entries: List<GeocodingEntity> = emptyList(),
    private val throwError: Boolean = false
) : GeocodingDao {

    private var geocodedAddressList = mutableListOf<GeocodingEntity>()
    private val defaultEntryCount = 50

    init {
        if (entries.isEmpty()) {
            repeat(defaultEntryCount) {
                geocodedAddressList.add(GeocodingGenerator.generateGeocodingEntities().first)
            }
        } else {
            geocodedAddressList.addAll(entries)
        }
    }

    override suspend fun upsert(geocodingEntity: GeocodingEntity) {
        if (throwError) throw IllegalArgumentException()
        geocodedAddressList =
            geocodedAddressList.filter { entity -> entity.placeId == geocodingEntity.placeId }
                .toMutableList()
        geocodedAddressList.add(geocodingEntity)
    }

    override suspend fun getAddress(
        lat: Double,
        lng: Double
    ): GeocodingEntity? {
        if (throwError) throw IllegalArgumentException()
        return geocodedAddressList.find { (_, latLng) -> latLng.lat == lat && latLng.lng == lng }
    }

    override suspend fun getAddress(address: String): GeocodingEntity? {
        if (throwError) throw IllegalArgumentException()
        return geocodedAddressList.find { (_, _, _, _, fullAddress) -> fullAddress == address }
    }

    override suspend fun clearAll() {
        if (throwError) throw IllegalArgumentException()
        geocodedAddressList.clear()
    }

    override suspend fun getCount(): Int {
        if (throwError) throw IllegalArgumentException()
        return geocodedAddressList.size
    }

    override suspend fun clearExpired(timestamp: Long) {
        if (throwError) throw IllegalArgumentException()
        geocodedAddressList =
            geocodedAddressList.filter { entity -> entity.createdTimestamp > timestamp }
                .toMutableList()
    }


}