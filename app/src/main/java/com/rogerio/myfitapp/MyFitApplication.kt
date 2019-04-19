package com.rogerio.myfitapp

import androidx.multidex.MultiDexApplication
import com.rogerio.myfitapp.di.fitModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber.DebugTree
import timber.log.Timber



class MyFitApplication: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        startKoin {
            // Android context
            androidContext(this@MyFitApplication)
            // modules
            modules(fitModule)
        }
    }
}