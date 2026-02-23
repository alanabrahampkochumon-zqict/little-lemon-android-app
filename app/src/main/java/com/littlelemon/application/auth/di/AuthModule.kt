package com.littlelemon.application.auth.di

import com.littlelemon.application.auth.data.AuthRepositoryImpl
import com.littlelemon.application.auth.data.remote.AuthRemoteDataSource
import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.SessionManagerImpl
import com.littlelemon.application.auth.domain.usecase.GetUserSessionStatusUseCase
import com.littlelemon.application.auth.domain.usecase.ResendOTPUseCase
import com.littlelemon.application.auth.domain.usecase.SaveUserInformationUseCase
import com.littlelemon.application.auth.domain.usecase.SendOTPUseCase
import com.littlelemon.application.auth.domain.usecase.ValidateEmailUseCase
import com.littlelemon.application.auth.domain.usecase.ValidateOTPUseCase
import com.littlelemon.application.auth.domain.usecase.VerifyOTPUseCase
import com.littlelemon.application.auth.presentation.AuthViewModel
import com.littlelemon.application.core.domain.SessionManager
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single<SessionManager> { SessionManagerImpl(get()) }

    viewModel<AuthViewModel> { AuthViewModel(get(), get(), get(), get(), get(), get()) }

    single { ValidateEmailUseCase() }
    single { ValidateOTPUseCase() }
    single { SendOTPUseCase(get()) }
    single { VerifyOTPUseCase(get()) }
    single { ResendOTPUseCase(get()) }
    single { SaveUserInformationUseCase(get()) }
    single { GetUserSessionStatusUseCase(get()) }

    single<AuthRepository> { AuthRepositoryImpl(get()) }

    single<AuthRemoteDataSource> { AuthRemoteDataSource(get()) }
}