package com.littlelemon.application.menu.di

import androidx.room.Room
import com.littlelemon.application.menu.data.DishRepositoryImpl
import com.littlelemon.application.menu.data.local.MenuDatabase
import com.littlelemon.application.menu.data.local.dao.DishDao
import com.littlelemon.application.menu.data.remote.MenuRemoteDataSource
import com.littlelemon.application.menu.data.remote.MenuRemoteDataSourceImpl
import com.littlelemon.application.menu.domain.DishRepository
import com.littlelemon.application.menu.domain.usecase.GetDishesUseCase
import com.littlelemon.application.menu.presentation.DishViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dishModule = module {
    viewModel<DishViewModel> {
        DishViewModel(get())
    }

    single<GetDishesUseCase> { GetDishesUseCase(get()) }

    single<DishRepository> {
        DishRepositoryImpl(get(), get())
    }

    single<DishDao> {
        Room.databaseBuilder(androidContext(), MenuDatabase::class.java, "dishes.db").build().dao
    }

    single<MenuRemoteDataSource> {
        MenuRemoteDataSourceImpl(get())
    }
}