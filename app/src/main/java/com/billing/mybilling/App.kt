package com.billing.mybilling

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App :Application(){
    override fun onCreate() {
        super.onCreate()
    }
}