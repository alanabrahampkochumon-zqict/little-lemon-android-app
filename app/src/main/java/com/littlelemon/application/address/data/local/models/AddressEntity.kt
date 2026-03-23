package com.littlelemon.application.address.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.littlelemon.application.address.data.local.converters.InstantConverter
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity
@TypeConverters(InstantConverter::class)
data class AddressEntity(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val label: String?,
    val address: String?,
    val streetAddress: String?,
    val city: String?,
    val state: String?,
    val pinCode: String?,
    val latitude: Double?,
    val longitude: Double?,
    val createdAt: Instant,
    val isDefault: Boolean = false,
)