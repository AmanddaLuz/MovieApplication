package com.amandaluz.movieapplication

import android.app.Application
import androidx.viewbinding.BuildConfig
import com.amandaluz.hawk.ModuleHawk
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
        ModuleHawk.init(applicationContext)

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
        }
    }
}