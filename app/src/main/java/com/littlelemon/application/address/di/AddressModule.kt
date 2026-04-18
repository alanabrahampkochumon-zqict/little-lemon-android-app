package com.littlelemon.application.address.di

import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.maps.GeoApiContext
import com.littlelemon.application.BuildConfig
import com.littlelemon.application.address.data.DefaultAddressRepository
import com.littlelemon.application.address.data.local.AddressDatabase
import com.littlelemon.application.address.data.local.AddressLocalDataSource
import com.littlelemon.application.address.data.local.DefaultAddressLocalDataSource
import com.littlelemon.application.address.data.local.dao.AddressDao
import com.littlelemon.application.address.data.local.dao.GeocodingDao
import com.littlelemon.application.address.data.remote.AddressRemoteDataSource
import com.littlelemon.application.address.data.remote.SupabaseAddressRemoteDataSource
import com.littlelemon.application.address.data.remote.geocoding.GeocodingEngine
import com.littlelemon.application.address.data.remote.geocoding.GeocodingRemoteDataSource
import com.littlelemon.application.address.data.remote.geocoding.GoogleGeocodingEngine
import com.littlelemon.application.address.data.remote.geocoding.GoogleGeocodingRemoteDataSource
import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.DefaultAddressManager
import com.littlelemon.application.address.domain.usecase.GeocodeAddressUseCase
import com.littlelemon.application.address.domain.usecase.GetAddressCountUseCase
import com.littlelemon.application.address.domain.usecase.GetAddressUseCase
import com.littlelemon.application.address.domain.usecase.GetLocationUseCase
import com.littlelemon.application.address.domain.usecase.ReverseGeocodeLocationUseCase
import com.littlelemon.application.address.domain.usecase.SaveAddressUseCase
import com.littlelemon.application.address.presentation.AddressViewModel
import com.littlelemon.application.core.domain.AddressManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val addressModule = module {
    viewModel<AddressViewModel> {
        AddressViewModel(get(), get(), get(), get(), get())
    }

    single<AddressManager> { DefaultAddressManager(get()) }

    single<GetLocationUseCase> { GetLocationUseCase(get()) }
    single<GetAddressCountUseCase> { GetAddressCountUseCase(get()) }
    single<GetAddressUseCase> { GetAddressUseCase(get()) }
    single<SaveAddressUseCase> { SaveAddressUseCase(get()) }
    single<ReverseGeocodeLocationUseCase> { ReverseGeocodeLocationUseCase(get()) }
    single<GeocodeAddressUseCase> { GeocodeAddressUseCase(get()) }

    single<AddressRepository> { DefaultAddressRepository(get(), get(), get(), get()) }

    single<AddressRemoteDataSource> { SupabaseAddressRemoteDataSource(get()) }
    single<AddressLocalDataSource> { DefaultAddressLocalDataSource(get(), get()) }

    single<FusedLocationProviderClient> {
        LocationServices.getFusedLocationProviderClient(androidContext())
    }

    single<AddressDao> {
        get<AddressDatabase>().addressDao
    }

    single<GeocodingDao> {
        get<AddressDatabase>().geocodingDao
    }
    single<AddressDatabase> {
        Room.databaseBuilder(androidContext(), AddressDatabase::class.java, "address.db").build()
    }

    single<GeocodingRemoteDataSource> {
        GoogleGeocodingRemoteDataSource(get())
    }

    single<GeocodingEngine> {
        GoogleGeocodingEngine(get())
    }

    single<GeoApiContext> {
        GeoApiContext.Builder().apiKey(BuildConfig.MAPS_API_KEY).build()
    }

}