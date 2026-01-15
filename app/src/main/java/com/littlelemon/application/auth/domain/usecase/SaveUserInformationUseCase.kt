package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.User
import com.littlelemon.application.auth.domain.usecase.params.UserInfoParams
import com.littlelemon.application.core.domain.UseCase
import com.littlelemon.application.core.domain.utils.Resource

class SaveUserInformationUseCase(private val repository: AuthRepository) :
    UseCase<UserInfoParams, Resource<Unit>> {
    override suspend fun invoke(input: UserInfoParams): Resource<Unit> =
        repository.saveUserInformation(input.firstName, input.lastName)

}

