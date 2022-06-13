package com.gimbal.kotlin.ouicannes

import android.app.Application
import com.gimbal.kotlin.ouicannes.utils.GimbalIntegration

class   OuiCannes : Application() {
    override fun onCreate() {
        super.onCreate()
        GimbalIntegration.init(this).onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
        GimbalIntegration.init(this).onTerminate()
    }
}