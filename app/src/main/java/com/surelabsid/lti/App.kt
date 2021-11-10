package com.surelabsid.lti

import android.app.Application
import android.content.ContextWrapper
import com.pixplicity.easyprefs.library.Prefs

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Prefs.Builder()
            .setContext(baseContext)
            .setUseDefaultSharedPreference(true)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .build()
    }
}