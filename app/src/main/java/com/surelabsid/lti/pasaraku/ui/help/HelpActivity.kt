package com.surelabsid.lti.pasaraku.ui.help

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.surelabsid.lti.base.Baseapp
import com.surelabsid.lti.pasaraku.databinding.ActivityHelpBinding
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.ResponseAppSettings
import com.surelabsid.lti.pasaraku.utils.Utils
import com.vanillaplacepicker.utils.ToastUtils
import kotlinx.coroutines.*

class HelpActivity : Baseapp() {

    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Help & Support"
            setDisplayHomeAsUpEnabled(true)
        }

        getAppSettings()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)

    }

    private fun getAppSettings() {
        showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val data = NetworkModule.getService().appSettings()
                    MainScope().launch {
                        dismissLoading()
                        updateUI(data)
                    }
                } catch (t: Throwable) {
                    t.printStackTrace()
                    MainScope().launch {
                        dismissLoading()
                        ToastUtils.showToast(this@HelpActivity, t.message)
                    }
                }
            }
        }
    }

    private fun updateUI(data: ResponseAppSettings) {
        binding.email.text = data.appSettings?.email
        binding.wa.text = data.appSettings?.noWa

        binding.wa.setOnClickListener {
            Utils.openWhatsApp(
                this@HelpActivity,
                binding.wa.text.toString(),
                "Saya butuh bantuan..."
            )
        }
    }
}