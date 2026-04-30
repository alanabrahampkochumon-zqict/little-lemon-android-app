package com.littlelemon.application.shared.auth.domain

import com.littlelemon.application.shared.auth.domain.model.SessionStatus
import kotlinx.coroutines.flow.Flow

interface SessionManager {
    fun getCurrentSessionStatus(): Flow<SessionStatus>
}