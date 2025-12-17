package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.User
import com.littlelemon.application.core.domain.UseCase
import com.littlelemon.application.core.domain.utils.Resource

class VerifyVerificationCodeUseCase(private val repository: AuthRepository) :
    UseCase<String, Resource<User>> {

    override suspend fun invoke(input: String): Resource<User> =
        repository.verifyVerificationCode(input)

}