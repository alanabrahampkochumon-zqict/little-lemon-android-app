package com.littlelemon.application.core.domain.model

sealed interface SessionStatus {
    // User session exists but user has some more things to do, in this app's case personalization
    data object PartiallyAuthenticated : SessionStatus

    // User session exists and is complete
    data object FullyAuthenticated : SessionStatus

    // User session does not exist
    data object Unauthenticated : SessionStatus

    // When the session is being fetch from database
    data object SessionLoading : SessionStatus
}