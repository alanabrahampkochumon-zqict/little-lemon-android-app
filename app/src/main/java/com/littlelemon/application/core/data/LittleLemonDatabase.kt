package com.littlelemon.application.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.littlelemon.application.auth.data.models.SessionToken

@Database(entities = [SessionToken::class], version = 1, exportSchema = false)
abstract class LittleLemonDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "little_lemon.db"
    }
}