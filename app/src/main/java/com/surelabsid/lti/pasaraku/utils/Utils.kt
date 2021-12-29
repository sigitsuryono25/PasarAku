package com.surelabsid.lti.pasaraku.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import com.surelabsid.lti.pasaraku.ui.login.LoginBottomSheet
import java.net.URLEncoder

object Utils {
    fun openWhatsApp(context: Context, numero: String, mensaje: String) {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            val url =
                "https://api.whatsapp.com/send?phone=$numero&text=" + URLEncoder.encode(
                    mensaje,
                    "UTF-8"
                )
            i.setPackage("com.whatsapp")
            i.data = Uri.parse(url)
            startActivity(context,i, null)
        } catch (e: Exception) {
            Log.e("ERROR WHATSAPP", e.toString())
            sendWa4Business(context, numero, mensaje)
        }
    }

    private fun sendWa4Business(context: Context, numero: String, mensaje: String) {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            val url =
                "https://api.whatsapp.com/send?phone=$numero&text=" + URLEncoder.encode(
                    mensaje,
                    "UTF-8"
                )
            i.setPackage("com.whatsapp.w4b")
            i.data = Uri.parse(url)
            startActivity(context,i, null)
        } catch (e: Exception) {
            Log.e("ERROR WHATSAPP", e.toString())
            AlertDialog.Builder(context)
                .setTitle("Kesalahan")
                .setMessage("Silahkan install Whatsapp atau Whatsapp Business terlebih dulu")
                .setPositiveButton("Okay"){
                        _,_->
                    val url =
                        "https://play.google.com/store/apps/details?id=com.whatsapp&hl=en&gl=US"
                    val playStore = Intent(Intent.ACTION_VIEW)
                    playStore.data = Uri.parse(url)
                    startActivity(context, playStore, null)
                }.create().show()
        }
    }

    fun showDialogLogin(supportFragmentManager: FragmentManager) {
        val loginDialog = LoginBottomSheet()
        loginDialog.show(supportFragmentManager, "dialogLogin")
    }
}