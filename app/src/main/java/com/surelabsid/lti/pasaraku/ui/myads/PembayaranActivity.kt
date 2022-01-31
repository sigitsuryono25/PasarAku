package com.surelabsid.lti.pasaraku.ui.myads

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import com.bumptech.glide.Glide
import com.surelabsid.lti.base.Baseapp
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ActivityPembayaranBinding
import com.surelabsid.lti.pasaraku.model.PremiumModel
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.DataIklanItem
import com.surelabsid.lti.pasaraku.response.ResponseRekening
import com.surelabsid.lti.pasaraku.ui.WebViewActivity
import com.surelabsid.lti.pasaraku.ui.iklan.DetailIklanActivity
import com.surelabsid.lti.pasaraku.utils.Constant
import com.surelabsid.lti.pasaraku.utils.GPSTracker
import com.surelabsid.lti.pasaraku.utils.HourToMillis
import kotlinx.coroutines.*
import java.util.*

class PembayaranActivity : Baseapp() {
    private lateinit var binding: ActivityPembayaranBinding
    private var premiumModel = PremiumModel()
    private var random: Random = Random()
    private var rand = random.nextInt(999).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        getRekening()
        getPaket()

        val dataIklanItem = intent.getParcelableExtra<DataIklanItem>(DetailIklanActivity.DATA_IKLAN)

        if (dataIklanItem?.foto?.isNotEmpty() == true) {
            val cover = dataIklanItem.foto.first()
            val urlGambar = Constant.ADS_PIC_URL + dataIklanItem.iklanId + "/" + cover
            Glide.with(this)
                .load(urlGambar)
                .into(binding.cover)
        }

        val location = Location(getString(R.string.app_name))
        location.latitude = dataIklanItem?.lat.toString().toDouble()
        location.longitude = dataIklanItem?.lon.toString().toDouble()

        binding.judulIklan.text = dataIklanItem?.judulIklan
        binding.harga.text = dataIklanItem?.harga

        val listAddress = GPSTracker(this).geocoder(location)
        if (listAddress.isNotEmpty()) {
            val kab = listAddress.iterator().next().subAdminArea
            val kec = listAddress.iterator().next().locality
            val prov = listAddress.iterator().next().adminArea
            binding.wilayahkerja.text = "$kec, $kab, $prov"
        } else {
            binding.wilayahkerja.text = "Indonesia"
        }

        binding.lanjut.setOnClickListener {
            dataIklanItem?.let { populateData(it) }
        }

        binding.rgAkun.setOnCheckedChangeListener { group, checkedId ->
            val selectedId = group.checkedRadioButtonId
            val radio = findViewById<RadioButton>(selectedId)
            val text = radio.text
            val span = text.split("\n")
            premiumModel.durasi = span[0].replace("hari", "days", true)
            premiumModel.paket = span[0]
            premiumModel.nominal = span[1].replace("Rp", "", true).replace(".", "", true).trim().toInt().minus(rand)
        }
    }

    private fun populateData(dataIklanItem: DataIklanItem) {
        if (premiumModel.bank == null || premiumModel.nominal == null) {
            Toast.makeText(
                this@PembayaranActivity,
                "Silahkan pilih pake dan rekening pembayaran terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            premiumModel.id = "INV-${HourToMillis.millis()}"
            premiumModel._id_ads = dataIklanItem.iklanId

            sendData()
        }
    }

    private fun sendData() {
        showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val sendData = NetworkModule.getService().requestPremium(premiumModel)
                    if (sendData.code == 200) {
                        //showinvoice here
                        MainScope().launch {
                            dismissLoading()
                            Intent(this@PembayaranActivity, WebViewActivity::class.java).apply {
                                putExtra(WebViewActivity.REKG, premiumModel)
                                startActivity(this)
                            }
                            finish()
                        }
                    } else {
                        MainScope().launch {
                            dismissLoading()
                            Toast.makeText(
                                this@PembayaranActivity,
                                sendData.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    MainScope().launch {
                        dismissLoading()
                        Toast.makeText(
                            this@PembayaranActivity,
                            e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun getPaket() {
        showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val paket = NetworkModule.getService().getPaket()
                if (paket.code == 200) {
                    MainScope().launch {
                        dismissLoading()
                        binding.rgAkun.removeAllViews()
                        paket.dataPaket?.forEach {
                            val setHarga = "<b>${it?.durasi}</b><br>Rp. ${it?.harga}"
                            val rb = RadioButton(this@PembayaranActivity)
                            rb.text = Html.fromHtml(setHarga)
                            val lParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                            lParams.setMargins(0, 15, 0, 0)
                            rb.layoutParams = lParams
                            binding.rgAkun.addView(rb, -1)
                        }
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                MainScope().launch {
                    dismissLoading()
                    Toast.makeText(this@PembayaranActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getRekening() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val rekening = NetworkModule.getService().getRekeningList()
                    MainScope().launch {
                        updateUI(rekening)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateUI(rekening: ResponseRekening) {
        dismissLoading()
        binding.rgRekening.removeAllViews()
        rekening.dataRekening.let { data ->
            data?.mapIndexed { _, it ->
                val rb = RadioButton(this)
                val bank =
                    "<span style='font-size=15px'><b>${it?.bank}<br>${it?.pemilik}</b></span><br>${it?.norek}"
                rb.setPadding(10, 10, 10, 10)
                rb.text = Html.fromHtml(bank)
                binding.rgRekening.addView(rb, -1)
            }
        }

        binding.rgRekening.setOnCheckedChangeListener { group, _ ->
            val selectedId: Int = group.checkedRadioButtonId
            val radioButton = findViewById<View>(selectedId) as RadioButton
            val text = radioButton.text
            val span = text.split("\n")
            premiumModel.bank = span[0]
            premiumModel.rekening = span[2]
            Log.d("updateUI", "updateUI: $span")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}