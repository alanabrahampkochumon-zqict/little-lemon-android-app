package com.littlelemon.application.home.di

import com.littlelemon.application.home.presentation.HomeViewModel
import org.koin.dsl.module

val homeModule = module {
    single<HomeViewModel> {
        HomeViewModel(get())
    }
}