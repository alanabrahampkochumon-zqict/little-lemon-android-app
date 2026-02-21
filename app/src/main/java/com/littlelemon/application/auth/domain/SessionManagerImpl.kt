package com.littlelemon.application.auth.domain

import com.littlelemon.application.auth.domain.models.UserSessionStatus
import com.littlelemon.application.auth.domain.usecase.GetUserSessionStatusUseCase
import com.littlelemon.application.core.domain.SessionManager
import com.littlelemon.application.core.domain.model.SessionStatus
import com.littlelemon.application.core.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SessionManagerImpl(private val getUserSessionUseCase: GetUserSessionStatusUseCase) :
    SessionManager {
    override fun getCurrentSessionStatus(): Flow<SessionStatus> {
        return getUserSessionUseCase().map { userSessionStatus ->
            if (userSessionStatus is Resource.Success) {
                when (val sessionStatus = userSessionStatus.data) {
                    is UserSessionStatus.Authenticated -> {
                        if (sessionStatus.userSession?.user?.firstName != null)
                            return@map SessionStatus.FullyAuthenticated
                        return@map SessionStatus.PartiallyAuthenticated
                    }

                    UserSessionStatus.Initializing -> return@map SessionStatus.SessionLoading
                    UserSessionStatus.Unauthenticated -> return@map SessionStatus.Unauthenticated
                    null -> return@map SessionStatus.Unauthenticated
                }
            }
            return@map SessionStatus.Unauthenticated
        }
    }
}