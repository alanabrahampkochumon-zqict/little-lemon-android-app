package com.littlelemon.application.database.di

import androidx.room.Room
import com.littlelemon.application.address.data.local.AddressDatabase
import com.littlelemon.application.database.address.dao.AddressDao
import com.littlelemon.application.database.address.dao.GeocodingDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


const val databaseName = "LittleLemonDatabase.db"

val databaseModule = module {
    
    ///// Address /////
    single<AddressDao> {
        get<AddressDatabase>().addressDao
    }

    single<GeocodingDao> {
        get<AddressDatabase>().geocodingDao
    }
    single<AddressDatabase> {
        Room.databaseBuilder(androidContext(), AddressDatabase::class.java, databaseName).build()
    }
}