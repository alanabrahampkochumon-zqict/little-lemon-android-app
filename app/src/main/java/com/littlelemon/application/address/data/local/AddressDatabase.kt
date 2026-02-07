package com.littlelemon.application.address.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.littlelemon.application.address.data.local.dao.AddressDao
import com.littlelemon.application.address.data.local.models.AddressEntity

@Database(entities = [AddressEntity::class], version = 1, exportSchema = false)
abstract class AddressDatabase : RoomDatabase() {
    abstract val dao: AddressDao
}