package com.littlelemon.application.address.di

import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.littlelemon.application.address.data.AddressRepositoryImpl
import com.littlelemon.application.address.data.local.AddressDatabase
import com.littlelemon.application.address.data.local.AddressLocalDataSource
import com.littlelemon.application.address.data.local.AddressLocalDataSourceImpl
import com.littlelemon.application.address.data.local.dao.AddressDao
import com.littlelemon.application.address.data.remote.AddressRemoteDataSource
import com.littlelemon.application.address.data.remote.AddressRemoteDataSourceImpl
import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.usecase.GetAddressUseCase
import com.littlelemon.application.address.domain.usecase.GetLocationUseCase
import com.littlelemon.application.address.domain.usecase.SaveAddressUseCase
import com.littlelemon.application.address.presentation.AddressViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val addressModule = module {
    viewModel<AddressViewModel> {
        AddressViewModel(get(), get(), get())
    }

    single<GetLocationUseCase> { GetLocationUseCase(get()) }
    single<GetAddressUseCase> { GetAddressUseCase(get()) }
    single<SaveAddressUseCase> { SaveAddressUseCase(get()) }

    single<AddressRepository> { AddressRepositoryImpl(get(), get()) }

    single<AddressRemoteDataSource> { AddressRemoteDataSourceImpl(get()) }
    single<AddressLocalDataSource> { AddressLocalDataSourceImpl(get(), get()) }

    single<FusedLocationProviderClient> {
        LocationServices.getFusedLocationProviderClient(androidContext())
    }

    single<AddressDao> {
        Room.databaseBuilder(androidContext(), AddressDatabase::class.java, "address.db")
            .build().dao
    }
}