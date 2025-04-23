package com.kurly.android

import android.app.Application
import timber.log.Timber

// Application 클래스에서 초기화

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
//        }
    }
}
