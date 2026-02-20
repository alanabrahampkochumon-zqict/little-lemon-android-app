package com.littlelemon.application.core.domain

import com.littlelemon.application.core.domain.model.SessionStatus

interface SessionManager {
    suspend fun getCurrentSessionStatus(): SessionStatus
}