package com.littlelemon.application.orders.di

import com.littlelemon.application.orders.presentation.screens.OrderScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val orderModule = module {
    viewModel<OrderScreenViewModel> {
        OrderScreenViewModel(get())
    }
}