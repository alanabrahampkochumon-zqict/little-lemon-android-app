package com.littlelemon.application

import android.app.Application
import com.littlelemon.application.address.di.addressModule
import com.littlelemon.application.auth.di.authModule
import com.littlelemon.application.cart.di.cartModule
import com.littlelemon.application.core.di.coreModule
import com.littlelemon.application.database.di.databaseModule
import com.littlelemon.application.home.di.homeModule
import com.littlelemon.application.menu.di.dishModule
import com.littlelemon.application.shared.cart.di.sharedCartModule
import com.littlelemon.application.shared.menu.di.sharedDishModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class LittleLemonApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@LittleLemonApplication)
            modules(sharedDishModule)
            modules(sharedCartModule)
            modules(databaseModule)
            modules(authModule)
            modules(coreModule)
            modules(addressModule)
            modules(dishModule)
            modules(homeModule)
            modules(cartModule)
        }
    }
}