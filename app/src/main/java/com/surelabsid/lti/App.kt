package com.surelabsid.lti

import android.app.Application
import android.content.ContextWrapper
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.utils.Constant

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Prefs.Builder()
            .setContext(baseContext)
            .setUseDefaultSharedPreference(true)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .build()

        generateToken()

    }


    private fun generateToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            Log.d("generateToken", "generateToken: ${it.result}")
            if (it.isSuccessful) {
                Prefs.putString(Constant.TOKEN, it.result)
            }
        }
    }
}