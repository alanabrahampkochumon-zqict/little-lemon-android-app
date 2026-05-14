package com.littlelemon.application.orders.di

import com.littlelemon.application.orders.presentation.screens.OrdersViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val orderModule = module {
    viewModel<OrdersViewModel> {
        OrdersViewModel(get(), get())
    }
}