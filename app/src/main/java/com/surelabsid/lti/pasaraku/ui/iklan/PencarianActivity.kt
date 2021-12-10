package com.surelabsid.lti.pasaraku.ui.iklan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.databinding.ActivityPencarianBinding
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.ResponseListIklan
import com.surelabsid.lti.pasaraku.ui.explore.adapter.AdapterIklan
import com.surelabsid.lti.pasaraku.ui.wilayah.WilayahActivity
import com.surelabsid.lti.pasaraku.ui.wilayah.WilayahViewModel
import com.surelabsid.lti.pasaraku.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PencarianActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPencarianBinding
    private lateinit var adapterIklan: AdapterIklan
    private lateinit var model: FilterViewModel
    private var provinsi: String? = null
    private var kabupaten: String? = null
    private var kecamatan: String? = null
    private var searchQuery: String? = null
    private lateinit var wilayahModel: WilayahViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPencarianBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchQuery = intent.getStringExtra(SEARCH_DATA)
        binding.searchQuery.setText(searchQuery)

        binding.lokasi.setOnClickListener {
            Intent(this, WilayahActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra(WilayahActivity.PROVINSI_REQ, true)
                startActivity(this)
            }
        }

        binding.filter.setOnClickListener {
            val filterDialog = FilterBottomSheet()
            filterDialog.show(supportFragmentManager, "filterDialog")
        }

        binding.back.setOnClickListener {
            finish()
        }



        adapterIklan = AdapterIklan(isGrid = false, onClick = {
            Intent(this@PencarianActivity, DetailIklanActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra(DetailIklanActivity.DATA_IKLAN, it)
                startActivity(this)
            }
        }, onFavClick = { data, img ->
        })

        binding.hasilPencarian.apply {
            adapter = adapterIklan
            layoutManager = LinearLayoutManager(this@PencarianActivity)
        }

        model = ViewModelProvider(this).get(FilterViewModel::class.java)

        model.kondisi.observe(this) { kondisi ->
            Log.d("kondisi", "onCreate: $kondisi")
            goFetchData(searchQuery, kondisi, provinsi, kabupaten, kecamatan)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        Prefs.remove(Constant.FROM_SEARCH)
    }

    override fun onResume() {
        super.onResume()
        kabupaten = Prefs.getString(Constant.KAB_ID)
        kecamatan = Prefs.getString(Constant.LOKASI_ID)
        provinsi = Prefs.getString(Constant.PROV_ID)

        val lokasi =
            if (Prefs.getString(Constant.KEC).isNotEmpty() || Prefs.contains(Constant.KEC)) {
                "${Prefs.getString(Constant.KEC)}, ${Prefs.getString(Constant.KAB)}"
            } else {
                Prefs.getString(Constant.KAB)
            }
        if (lokasi.isNotEmpty()) {
            binding.lokasi.text = lokasi
        }else{
            binding.lokasi.text = "Indonesia"
        }

        goFetchData(searchQuery, kabupaten = kabupaten, provinsi = provinsi, kecamatan = kecamatan)

    }

    private fun goFetchData(
        searchQuery: String?,
        kondisi: String? = null,
        provinsi: String? = null,
        kabupaten: String? = null,
        kecamatan: String? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val data = NetworkModule.getService()
                        .searchIklan(
                            query = searchQuery,
                            kondisi = kondisi,
                            provinsi = provinsi,
                            kabupaten = kabupaten,
                            kecamatan = kecamatan
                        )
                    withContext(Dispatchers.Main) {
                        updateUi(data)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@PencarianActivity, e.message, Toast.LENGTH_SHORT).show()
                        adapterIklan.addIklan(listOf(), true)
                    }
                }
            }
        }
    }

    private fun updateUi(responseListIklan: ResponseListIklan) {
        binding.progressBar.visibility = View.GONE
        responseListIklan.dataIklan?.let { adapterIklan.addIklan(it, clearIt = true) }
        binding.hasilPencarian.visibility = View.VISIBLE
    }

    companion object {
        const val SEARCH_DATA = "searchData"
    }
}