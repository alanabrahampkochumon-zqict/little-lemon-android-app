package com.littlelemon.application.database.di

import androidx.room.Room
import com.littlelemon.application.database.LittleLemonDatabase
import com.littlelemon.application.database.address.dao.AddressDao
import com.littlelemon.application.database.address.dao.GeocodingDao
import com.littlelemon.application.database.cart.CartDao
import com.littlelemon.application.database.menu.MenuDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


const val databaseName = "LittleLemonDatabase.db"

val databaseModule = module {

    ///// Address /////
    single<AddressDao> {
        get<LittleLemonDatabase>().addressDao
    }

    single<GeocodingDao> {
        get<LittleLemonDatabase>().geocodingDao
    }


    ///// Cart /////
    single<CartDao> {
        get<LittleLemonDatabase>().cartDao
    }


    ///// Menu /////
    single<MenuDao> {
        get<LittleLemonDatabase>().menuDao
    }


    ///// Database /////
    single<LittleLemonDatabase> {
        Room.databaseBuilder(androidContext(), LittleLemonDatabase::class.java, databaseName)
            .build()
    }
}