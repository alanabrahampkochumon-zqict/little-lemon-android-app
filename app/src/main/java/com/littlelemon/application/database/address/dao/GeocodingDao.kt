package com.littlelemon.application.database.address.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.littlelemon.application.database.address.models.GeocodingEntity

@Dao
interface GeocodingDao {

    @Upsert
    suspend fun upsert(geocodingEntity: GeocodingEntity)

    @Query("SELECT * FROM GEOCODINGENTITY WHERE abs(loc_lat - :lat) < 0.0001 AND abs(loc_lng - :lng) < 0.0001 LIMIT 1")
    suspend fun getAddress(lat: Double, lng: Double): GeocodingEntity?

    @Query("SELECT * FROM GEOCODINGENTITY WHERE fullAddress = :address LIMIT 1")
    suspend fun getAddress(address: String): GeocodingEntity?

    @Query("DELETE FROM GEOCODINGENTITY")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM GEOCODINGENTITY")
    suspend fun getCount(): Int

    @Query("DELETE FROM GEOCODINGENTITY WHERE createdTimestamp <= :timestamp")
    suspend fun clearExpired(timestamp: Long)
}