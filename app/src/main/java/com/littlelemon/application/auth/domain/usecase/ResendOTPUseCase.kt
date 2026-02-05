package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.core.domain.utils.Resource

class ResendOTPUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(emailAddress: String): Resource<Unit> =
        repository.resendOTP(emailAddress)
}