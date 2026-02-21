package com.littlelemon.application.core.di

import com.littlelemon.application.core.presentation.RootViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val coreModule = module {
    viewModel<RootViewModel> { RootViewModel(get()) }
}