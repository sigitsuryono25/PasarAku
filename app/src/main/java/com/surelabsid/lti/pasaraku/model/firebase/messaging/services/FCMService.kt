@file:Suppress("DEPRECATION")

package com.surelabsid.lti.pasaraku.model.firebase.messaging.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.database.AppDatabase
import com.surelabsid.lti.pasaraku.database.Notifications
import com.surelabsid.lti.pasaraku.model.firebase.model.ChatHeader
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.ui.akun.transaksi.TransaksiActivity
import com.surelabsid.lti.pasaraku.ui.notification.NotificationActivity
import com.surelabsid.lti.pasaraku.utils.Constant
import com.surelabsid.lti.pasaraku.utils.HourToMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FCMService : FirebaseMessagingService() {
    private val mRoutes = NetworkModule.getService()
    private val job = CoroutineScope(Dispatchers.Default)

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("onNewToken", "onNewToken: $p0")
        try {
            job.launch(Dispatchers.IO) {
                mRoutes.updateToken(p0, Prefs.getString(Constant.EMAIL, null))
            }
            Prefs.putString(Constant.TOKEN, p0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        val db =
            Room.databaseBuilder(this@FCMService, AppDatabase::class.java, Constant.DB_NAME).build()

        Log.d("message", p0.data.toString())
        if (p0.data.isNotEmpty()) {
            if (Prefs.contains(Constant.EMAIL)) {
                val data = p0.data
                val pengirim = data["nama"]
                val isAdmin = data["is_admin"]
                val message = data["message"]
                if (isAdmin?.equals("false", true) == true) {
                    val chatHeader = ChatHeader()
                    chatHeader.nama = pengirim
                    chatHeader._id = HourToMillis.millis()
                    chatHeader.token = ""
                    chatHeader.user_id = data["user_id"]
                    NotificationHandle(this).makeNotificationStart(pengirim, chatHeader, db)
                } else {
                    NotificationHandle(this).makeNotificationStart(
                        from = pengirim,
                        chatHeader = null,
                        db = db,
                        message = "$message ",
                        title = "Notifikasi Baru",
                        transaksi = true
                    )
                }
            }
        }
    }

}

class NotificationHandle(
    private val context: Context,
    private val id: Int = Math.random().toInt()
) {
    private val cHANNELID = "myChanel"

    init {
        createNotificationChannel()
    }

    fun makeNotificationStart(
        from: String?,
        chatHeader: ChatHeader?,
        db: AppDatabase,
        message: String? = "Chat Baru dari ",
        title: String? = "Chat Baru",
        transaksi: Boolean = false
    ) {
        val pendingIntentChat = if (transaksi) {
            val intentTransaksi = Intent(context, TransaksiActivity::class.java)
            PendingIntent.getActivity(
                context,
                1036,
                intentTransaksi,
                PendingIntent.FLAG_ONE_SHOT
            )
        } else {
            val intentChat = Intent(context, NotificationActivity::class.java)
            if (chatHeader != null)
                intentChat.putExtra(Constant.CHAT_HEADER, chatHeader)
            PendingIntent.getActivity(context, 1025, intentChat, PendingIntent.FLAG_ONE_SHOT)
        }
        val builder = NotificationCompat.Builder(context, cHANNELID)
            .setSmallIcon(android.R.drawable.ic_dialog_email)
            .setColor(context.resources.getColor(R.color.darkblue))
            .setContentTitle(title)
            .setContentText("$message$from")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntentChat)
            .setAutoCancel(true)


        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val notifications = Notifications(
                        timestamp = HourToMillis.millisToDateHour(HourToMillis.millis()),
                        message = "$message$from",
                        title = "$title",
                        transaksi = transaksi.toString()
                    )
                    val d = db.notificationDao().insertAll(notifications)
                    Log.d("makeNotificationStart", "makeNotificationStart: $d")
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }


        NotificationManagerCompat.from(context).apply {
            notify(id, builder.build())
        }

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "lauwba_channel"
            val descriptionText = "lauwba_channel_desc"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(cHANNELID, name, importance).apply {
                description = descriptionText

            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}