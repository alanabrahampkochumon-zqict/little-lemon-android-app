package com.littlelemon.application.address.data.local.converters

import androidx.room.TypeConverter
import kotlin.time.Instant

class InstantConverter {

    @TypeConverter
    fun fromInstant(instant: Instant?): Long? {
        return instant?.toEpochMilliseconds()
    }

    @TypeConverter
    fun toInstant(value: Long?): Instant? {
        return value?.let { Instant.fromEpochSeconds(it) }
    }
}