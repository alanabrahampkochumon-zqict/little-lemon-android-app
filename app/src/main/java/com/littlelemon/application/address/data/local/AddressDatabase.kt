package com.littlelemon.application.address.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.littlelemon.application.address.data.local.dao.AddressDao
import com.littlelemon.application.address.data.local.dao.GeocodingDao
import com.littlelemon.application.address.data.local.models.AddressEntity
import com.littlelemon.application.address.data.local.models.GeocodingEntity

@Database(
    entities = [AddressEntity::class, GeocodingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AddressDatabase : RoomDatabase() {
    abstract val addressDao: AddressDao
    abstract val geocodingDao: GeocodingDao
}