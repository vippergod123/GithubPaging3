package com.example.githubsample

import android.app.Application
import com.example.githubsample.di.getDi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {

        startKoin {
            androidContext(this@App)
//            androidLogger(Level.DEBUG)
            modules(getDi())
        }
        plantTimberTrees()
        super.onCreate()
    }

    private fun plantTimberTrees() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}