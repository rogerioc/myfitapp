package com.rogerio.myafitapp

import androidx.multidex.MultiDexApplication
import com.rogerio.myafitapp.di.fitModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyFitApplication: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Android context
            androidContext(this@MyFitApplication)
            // modules
            modules(fitModule)
        }
    }
}