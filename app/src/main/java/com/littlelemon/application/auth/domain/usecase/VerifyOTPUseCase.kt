package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.usecase.params.VerificationParams
import com.littlelemon.application.core.domain.UseCase
import com.littlelemon.application.core.domain.utils.Resource

class VerifyOTPUseCase(private val repository: AuthRepository) :
    UseCase<VerificationParams, Resource<Unit>> {

    override suspend fun invoke(input: VerificationParams): Resource<Unit> =
        repository.verifyVerificationCode(
            emailAddress = input.emailAddress,
            verificationCode = input.verificationCode
        )

}