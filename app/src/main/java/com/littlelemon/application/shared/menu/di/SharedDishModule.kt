package com.littlelemon.application.shared.menu.di

import com.littlelemon.application.shared.menu.data.DefaultMenuRepository
import com.littlelemon.application.shared.menu.data.remote.MenuRemoteDataSource
import com.littlelemon.application.shared.menu.data.remote.SupabaseMenuRemoteDataSource
import com.littlelemon.application.shared.menu.domain.MenuRepository
import com.littlelemon.application.shared.menu.domain.usecase.GetCategoriesUseCase
import com.littlelemon.application.shared.menu.domain.usecase.GetDishesUseCase
import org.koin.dsl.module

val sharedDishModule = module {
    single<GetCategoriesUseCase> { GetCategoriesUseCase(get()) }

    single<GetDishesUseCase> { GetDishesUseCase(get()) }

    single<MenuRepository> {
        DefaultMenuRepository(get(), get())
    }

    single<MenuRemoteDataSource> {
        SupabaseMenuRemoteDataSource(get())
    }
}