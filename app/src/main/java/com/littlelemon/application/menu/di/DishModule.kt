package com.littlelemon.application.menu.di

import androidx.room.Room
import com.littlelemon.application.menu.data.DefaultMenuRepository
import com.littlelemon.application.menu.data.local.MenuDatabase
import com.littlelemon.application.menu.data.local.dao.MenuDao
import com.littlelemon.application.menu.data.remote.MenuRemoteDataSource
import com.littlelemon.application.menu.data.remote.MenuRemoteDataSourceImpl
import com.littlelemon.application.menu.domain.MenuRepository
import com.littlelemon.application.menu.domain.usecase.GetCategoriesUseCase
import com.littlelemon.application.menu.domain.usecase.GetDishesUseCase
import com.littlelemon.application.menu.presentation.MenuViewModel
import org.koin.android.ext.koin.androidContext
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

    single<MenuDao> {
        Room.databaseBuilder(androidContext(), MenuDatabase::class.java, "dishes.db").build().dao
    }

    single<MenuRemoteDataSource> {
        MenuRemoteDataSourceImpl(get())
    }
}