package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.core.domain.UseCase
import com.littlelemon.application.core.domain.utils.Resource

class SendVerificationCodeUseCase(
    private val repository: AuthRepository
) : UseCase<String, Resource<Unit>> {

    override suspend fun invoke(input: String): Resource<Unit> {
        return repository.sendVerificationCode(input)
    }

}