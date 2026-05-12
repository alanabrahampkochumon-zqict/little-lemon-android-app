package com.littlelemon.application.profile.di

import com.littlelemon.application.profile.data.DefaultProfileRepository
import com.littlelemon.application.profile.data.remote.SupabaseProfileRemoteDataSource
import com.littlelemon.application.profile.domain.ProfileRepository
import com.littlelemon.application.profile.presentation.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    viewModel<ProfileViewModel> {
        ProfileViewModel(get())
    }

    single<ProfileRepository> {
        DefaultProfileRepository(get())
    }

    single<SupabaseProfileRemoteDataSource> {
        SupabaseProfileRemoteDataSource(get())
    }
}