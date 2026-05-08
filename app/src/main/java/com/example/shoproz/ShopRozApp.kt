package com.example.shoproz

import android.app.Application
import com.example.shoproz.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ShopRozApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ShopRozApp)
            modules(appModule)
        }
    }
}
