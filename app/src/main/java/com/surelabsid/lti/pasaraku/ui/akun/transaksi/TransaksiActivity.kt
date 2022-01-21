package com.surelabsid.lti.pasaraku.ui.akun.transaksi

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.base.Baseapp
import com.surelabsid.lti.pasaraku.databinding.ActivityTransaksiBinding
import com.surelabsid.lti.pasaraku.model.PremiumModel
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.ResponseTransaksi
import com.surelabsid.lti.pasaraku.ui.WebViewActivity
import com.surelabsid.lti.pasaraku.ui.akun.transaksi.adapter.AdapterTransaksi
import com.surelabsid.lti.pasaraku.utils.Constant
import com.vanillaplacepicker.utils.ToastUtils
import kotlinx.coroutines.*

class TransaksiActivity : Baseapp() {
    private lateinit var binding: ActivityTransaksiBinding
    private lateinit var mAdapterTransaksi: AdapterTransaksi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Transaksi Kamu"
        }
        val userid = Prefs.getString(Constant.EMAIL)
        getTrans(userid)

        mAdapterTransaksi = AdapterTransaksi {
            val premiumModel = PremiumModel()
            premiumModel.id = it?.id
            Intent(this@TransaksiActivity, WebViewActivity::class.java).apply {
                putExtra(WebViewActivity.REKG, premiumModel)
                startActivity(this)
            }
        }

        binding.rvTrans.apply {
            adapter = mAdapterTransaksi
            addItemDecoration(
                DividerItemDecoration(
                    this@TransaksiActivity,
                    LinearLayoutManager.VERTICAL
                )
            )
            layoutManager =
                LinearLayoutManager(this@TransaksiActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getTrans(userid: String?) {
        showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val resp = NetworkModule.getService().getTransaksi(userid)
                    MainScope().launch {
                        dismissLoading()
                        updateUI(resp)
                    }
                } catch (e: Throwable) {
                    MainScope().launch {
                        dismissLoading()
                        ToastUtils.showToast(this@TransaksiActivity, e.message)
                    }
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateUI(resp: ResponseTransaksi) {
        resp.dataTrans?.let { mAdapterTransaksi.addItem(it) }
    }
}