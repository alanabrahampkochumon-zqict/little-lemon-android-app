package com.littlelemon.application.cart.di

import com.littlelemon.application.cart.data.DefaultCartRepository
import com.littlelemon.application.cart.data.remote.CartRemoteDataSource
import com.littlelemon.application.cart.data.remote.SupabaseCartRemoteDataSource
import com.littlelemon.application.cart.domain.CartRepository
import com.littlelemon.application.cart.domain.usecase.ClearCartUseCase
import com.littlelemon.application.cart.domain.usecase.GetCartErrorMessagesUseCase
import com.littlelemon.application.cart.domain.usecase.GetCartItemsUseCase
import com.littlelemon.application.cart.domain.usecase.UpsertCartItemUseCase
import org.koin.dsl.module


val cartModule = module {

    single<UpsertCartItemUseCase> { UpsertCartItemUseCase(get()) }

    single<ClearCartUseCase> { ClearCartUseCase(get()) }

    single<GetCartErrorMessagesUseCase> { GetCartErrorMessagesUseCase(get()) }

    single<GetCartItemsUseCase> {
        GetCartItemsUseCase(get())
    }

    single<CartRepository> {
        DefaultCartRepository(get(), get())
    }

    single<CartRemoteDataSource> {
        SupabaseCartRemoteDataSource(get())
    }
}