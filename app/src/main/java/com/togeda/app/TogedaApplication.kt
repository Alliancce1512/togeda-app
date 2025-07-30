package com.togeda.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.togeda.app.di.appModule

class TogedaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TogedaApplication)
            modules(appModule)
        }
    }
}
