package com.littlelemon.application.core.di

import com.littlelemon.application.BuildConfig
import com.littlelemon.application.core.presentation.RootViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val coreModule = module {
    viewModel<RootViewModel> { RootViewModel(get()) }

    single<SupabaseClient> {
        createSupabaseClient(
            supabaseKey = BuildConfig.SUPABASE_KEY,
            supabaseUrl = BuildConfig.SUPABASE_URL
        ) {
            install(Auth) {}
            install(Postgrest) {}
            defaultSerializer = KotlinXSerializer(Json { ignoreUnknownKeys = true })
        }
    }
}