package com.littlelemon.application.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.littlelemon.application.database.address.dao.AddressDao
import com.littlelemon.application.database.address.dao.GeocodingDao
import com.littlelemon.application.database.address.models.AddressEntity
import com.littlelemon.application.database.address.models.GeocodingEntity


@Database(
    entities = [AddressEntity::class, GeocodingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LittleLemonDatabase : RoomDatabase() {

    abstract val addressDao: AddressDao
    abstract val geocodingDao: GeocodingDao
}