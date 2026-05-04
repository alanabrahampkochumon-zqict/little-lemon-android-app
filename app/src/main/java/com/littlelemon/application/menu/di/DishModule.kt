package com.littlelemon.application.menu.di

import com.littlelemon.application.menu.presentation.MenuViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dishModule = module {
    viewModel<MenuViewModel> {
        MenuViewModel(get(), get(), get(), get())
    }
}