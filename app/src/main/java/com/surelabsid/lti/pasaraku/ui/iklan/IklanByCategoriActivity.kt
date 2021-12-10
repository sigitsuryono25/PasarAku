package com.surelabsid.lti.pasaraku.ui.iklan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ActivityIklanByCategoriBinding
import com.surelabsid.lti.pasaraku.response.DataKategoriItem
import com.surelabsid.lti.pasaraku.response.ResponseListIklan
import com.surelabsid.lti.pasaraku.ui.explore.ExploreViewModel
import com.surelabsid.lti.pasaraku.ui.explore.adapter.AdapterIklan
import com.surelabsid.lti.pasaraku.utils.Constant

class IklanByCategoriActivity : AppCompatActivity() {

    private lateinit var adapterIklan: AdapterIklan
    private lateinit var binding: ActivityIklanByCategoriBinding
    private lateinit var vm: ExploreViewModel
    private var kategori : DataKategoriItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIklanByCategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this).get(ExploreViewModel::class.java)

        adapterIklan = AdapterIklan(isGrid = false, onClick = {
            Intent(this, DetailIklanActivity::class.java).apply {
                putExtra(DetailIklanActivity.DATA_IKLAN, it)
                startActivity(this)
            }
        }, onFavClick = { data, img ->
            Glide.with(this)
                .load(R.drawable.ic_baseline_favorite)
                .into(img)
        })

        kategori = intent.getParcelableExtra<DataKategoriItem>(ITEM_KATEGORI)

        binding.listIklan.apply {
            adapter = adapterIklan
            layoutManager = LinearLayoutManager(this@IklanByCategoriActivity)
        }

        binding.title.text = kategori?.namaKategori

        binding.back.setOnClickListener {
            finish()
        }
        vm.dataIklan.observe(this) {
            setToView(it)
        }
        vm.error.observe(this) {
            setError(it)
        }
    }

    override fun onResume() {
        super.onResume()
        val lokasi =
            if (Prefs.getString(Constant.KEC).isNotEmpty() || Prefs.contains(Constant.KEC)) {
                "${Prefs.getString(Constant.KEC)}, ${Prefs.getString(Constant.KAB)}"
            } else {
                Prefs.getString(Constant.KAB)
            }
        if (lokasi.isNotEmpty()) {
            var user: String? = null
            if (Prefs.contains(Constant.EMAIL)) {
                user = Prefs.getString(Constant.EMAIL)
            }
            vm.getListIklan(
                kategori = kategori?.idKategori,
                provinsi = Prefs.getString(Constant.PROV_ID),
                kabupaten = Prefs.getString(Constant.KAB_ID),
                kecamatan = Prefs.getString(Constant.LOKASI_ID),
                userid = user
            )
        } else {
            vm.getListIklan()
        }
    }

    private fun setError(throwable: Throwable) {
        Toast.makeText(this, throwable.message.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun setToView(responseListIklan: ResponseListIklan) {
        val iklanList = responseListIklan.dataIklan
        iklanList?.let { adapterIklan.addIklan(it, true) }
    }

    companion object {
        const val ITEM_KATEGORI = "itemKategori"
    }
}