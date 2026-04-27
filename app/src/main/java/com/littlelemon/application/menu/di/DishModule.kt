package com.littlelemon.application.menu.di

import com.littlelemon.application.menu.data.DefaultMenuRepository
import com.littlelemon.application.menu.data.remote.MenuRemoteDataSource
import com.littlelemon.application.menu.data.remote.SupabaseMenuRemoteDataSource
import com.littlelemon.application.menu.domain.MenuRepository
import com.littlelemon.application.menu.domain.usecase.GetCategoriesUseCase
import com.littlelemon.application.menu.domain.usecase.GetDishesUseCase
import com.littlelemon.application.menu.presentation.MenuViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dishModule = module {
    viewModel<MenuViewModel> {
        MenuViewModel(get(), get())
    }

    single<GetCategoriesUseCase> { GetCategoriesUseCase(get()) }

    single<GetDishesUseCase> { GetDishesUseCase(get()) }

    single<MenuRepository> {
        DefaultMenuRepository(get(), get())
    }

    single<MenuRemoteDataSource> {
        SupabaseMenuRemoteDataSource(get())
    }
}