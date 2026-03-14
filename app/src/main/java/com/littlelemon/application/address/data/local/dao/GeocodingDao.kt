package com.littlelemon.application.address.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.littlelemon.application.address.data.local.models.AddressEntity
import com.littlelemon.application.address.data.local.models.GeocodingEntity

@Dao
interface GeocodingDao {

    @Upsert
    suspend fun upsert(geocodingEntity: GeocodingEntity)

    @Query("SELECT * FROM GEOCODINGENTITY WHERE abs(loc_lat - :lat) > 0.0001 AND abs(loc_lng - :lng) > 0.0001")
    suspend fun getAddress(lat: Double, lng: Double): AddressEntity

    @Query("SELECT * FROM GEOCODINGENTITY WHERE fullAddress = :address")
    suspend fun getAddress(address: String): AddressEntity

    @Query("DELETE FROM GEOCODINGENTITY")
    suspend fun clearAll()

    @Query("DELETE FROM GEOCODINGENTITY WHERE createdTimestamp < :timestamp")
    suspend fun clearExpired(timestamp: Long)
}