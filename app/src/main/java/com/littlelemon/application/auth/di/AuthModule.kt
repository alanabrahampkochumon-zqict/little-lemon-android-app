package com.littlelemon.application.auth.di

import com.littlelemon.application.auth.data.AuthRepositoryImpl
import com.littlelemon.application.auth.data.remote.AuthRemoteDataSource
import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.usecase.ResendOTPUseCase
import com.littlelemon.application.auth.domain.usecase.SaveUserInformationUseCase
import com.littlelemon.application.auth.domain.usecase.SendOTPUseCase
import com.littlelemon.application.auth.domain.usecase.ValidateEmailUseCase
import com.littlelemon.application.auth.domain.usecase.ValidateOTPUseCase
import com.littlelemon.application.auth.domain.usecase.VerifyOTPUseCase
import com.littlelemon.application.auth.presentation.AuthViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single { ValidateEmailUseCase() }
    single { ValidateOTPUseCase() }
    single { SendOTPUseCase(get()) }
    single { VerifyOTPUseCase(get()) }
    single { ResendOTPUseCase(get()) }
    single { SaveUserInformationUseCase(get()) }

    single<AuthRepository> { AuthRepositoryImpl(get()) }

    single<AuthRemoteDataSource> { AuthRemoteDataSource(get()) }

    single<SupabaseClient> {
        createSupabaseClient(supabaseKey = "", supabaseUrl = "") {
            defaultSerializer = KotlinXSerializer(Json { })
        }
    }

    viewModel<AuthViewModel> { AuthViewModel(get(), get(), get(), get(), get(), get()) }
}