package com.togeda.app

import android.app.Application
import com.togeda.app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TogedaApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TogedaApplication)
            modules(appModule)
        }
    }
}
