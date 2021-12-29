package com.surelabsid.lti.pasaraku.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TokenService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.Default).launch {
            try{
                val userid = Prefs.getString(Constant.EMAIL)
                val token = Prefs.getString(Constant.TOKEN)
                val res = NetworkModule.getService().updateToken(token, userid)
                Log.d("onStartCommand", "onStartCommand: ${res.message}")
            }catch (t: Throwable){
                t.printStackTrace()
            }
        }
        return START_STICKY
    }
}