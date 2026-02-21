package com.littlelemon.application.core.domain

import com.littlelemon.application.core.domain.model.SessionStatus
import kotlinx.coroutines.flow.Flow

interface SessionManager {
    suspend fun getCurrentSessionStatus(): Flow<SessionStatus>
}