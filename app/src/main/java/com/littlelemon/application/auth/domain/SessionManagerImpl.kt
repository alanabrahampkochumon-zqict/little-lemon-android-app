package com.littlelemon.application.auth.domain

import com.littlelemon.application.auth.domain.usecase.GetUserSessionUseCase
import com.littlelemon.application.core.domain.SessionManager
import com.littlelemon.application.core.domain.model.SessionStatus
import kotlinx.coroutines.flow.Flow

class SessionManagerImpl(private val getUserSessionUseCase: GetUserSessionUseCase) :
    SessionManager {
    override suspend fun getCurrentSessionStatus(): Flow<SessionStatus> {
        TODO()
//        val userSessionResult = getUserSessionUseCase()
//        if (userSessionResult is Resource.Success) {
//            if (userSessionResult.data == null)
//                return SessionStatus.Unauthenticated
//
//            val (_, _, _, user) = userSessionResult.data
//            if (user.firstName == null)
//                return SessionStatus.PartiallyAuthenticated
//            return SessionStatus.FullyAuthenticated
//        }
//        return SessionStatus.Unauthenticated
    }
}