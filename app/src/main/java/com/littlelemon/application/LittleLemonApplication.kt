package com.littlelemon.application

import android.app.Application
import com.littlelemon.application.auth.di.authModule
import com.littlelemon.application.core.di.coreModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class LittleLemonApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@LittleLemonApplication)
            modules(authModule)
            modules(coreModule)
        }
    }
}