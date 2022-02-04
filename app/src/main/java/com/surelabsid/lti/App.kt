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


        Prefs.remove(Constant.IS_EDIT)
        Prefs.remove(Constant.PROV_ID_EDIT)
        Prefs.remove(Constant.PROV_EDIT)
        Prefs.remove(Constant.KAB_EDIT)
        Prefs.remove(Constant.KAB_ID_EDIT)
        Prefs.remove(Constant.KEC_EDIT)
        Prefs.remove(Constant.LOKASI_ID_EDIT)

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