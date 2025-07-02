package org.darthacheron.fitbe

import android.app.Application
import org.darthacheron.fitbe.di.initKoin
import org.koin.android.ext.koin.androidContext

class FitBeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@FitBeApplication)
        }
    }
}