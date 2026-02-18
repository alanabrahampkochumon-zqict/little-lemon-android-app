package com.littlelemon.application

import android.app.Application
import com.littlelemon.application.auth.di.authModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class LittleLemonApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Start Koin if not yet initialized
        // Prevents test crashes with Robolectric
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidLogger()
                androidContext(this@LittleLemonApplication)
                modules(authModule)
            }
        }
    }
}