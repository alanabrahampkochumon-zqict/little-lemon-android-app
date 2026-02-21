package com.littlelemon.application.core.domain

import com.littlelemon.application.core.domain.model.SessionStatus
import kotlinx.coroutines.flow.Flow

interface SessionManager {
    fun getCurrentSessionStatus(): Flow<SessionStatus>
}