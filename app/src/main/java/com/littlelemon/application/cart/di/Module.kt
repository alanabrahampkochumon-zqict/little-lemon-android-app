package com.littlelemon.application.cart.di

import com.littlelemon.application.cart.presentation.CartViewModel
import com.littlelemon.application.shared.cart.domain.usecase.RefreshCartUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val cartModule = module {
    viewModel<CartViewModel> {
        CartViewModel(get(), get(), get(), get(), get())
    }

    single<RefreshCartUseCase> { RefreshCartUseCase(get()) }
}