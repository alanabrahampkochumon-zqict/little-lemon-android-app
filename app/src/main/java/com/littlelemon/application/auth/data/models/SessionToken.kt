package com.littlelemon.application.auth.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
import kotlin.uuid.Uuid

@Entity
data class SessionToken(
    @PrimaryKey val id: UUID
)