package com.surelabsid.lti.pasaraku.ui.notification

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.surelabsid.lti.pasaraku.database.AppDatabase
import com.surelabsid.lti.pasaraku.database.Notifications
import com.surelabsid.lti.pasaraku.databinding.ActivityNotificationBinding
import com.surelabsid.lti.pasaraku.ui.akun.transaksi.TransaksiFragment
import com.surelabsid.lti.pasaraku.ui.notification.adapter.AdapterNotification
import com.surelabsid.lti.pasaraku.utils.Constant
import kotlinx.coroutines.*

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private lateinit var adapterNotification: AdapterNotification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = "Notification"
            setDisplayHomeAsUpEnabled(true)
        }

        val db = Room.databaseBuilder(this, AppDatabase::class.java, Constant.DB_NAME).build()

        adapterNotification = AdapterNotification {
            Intent(this@NotificationActivity, TransaksiFragment::class.java).apply {
                startActivity(this)
            }
            finish()
        }
        binding.rvNotification.apply {
            adapter = adapterNotification
            layoutManager = LinearLayoutManager(this@NotificationActivity)
        }

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val data = db.notificationDao().getAll()
                    MainScope().launch {
                        updateUI(data)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateUI(data: List<Notifications>) {
        adapterNotification.addItem(data)
    }
}