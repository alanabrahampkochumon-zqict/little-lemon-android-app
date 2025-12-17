package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.usecase.params.UserInfoParams
import com.littlelemon.application.core.domain.UseCase
import com.littlelemon.application.core.domain.utils.Resource

class SaveUserInformationUseCase(private val repository: AuthRepository) :
    UseCase<UserInfoParams, Resource<Unit>> {
    override fun invoke(input: UserInfoParams): Resource<Unit> {
        TODO()
    }

}

