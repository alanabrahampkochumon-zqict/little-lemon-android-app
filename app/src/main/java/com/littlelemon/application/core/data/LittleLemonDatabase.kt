package com.littlelemon.application.core.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [], version = 1, exportSchema = false)
abstract class LittleLemonDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "little_lemon.db"
    }
}