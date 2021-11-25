package com.surelabsid.lti.pasaraku.ui.iklan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.surelabsid.lti.pasaraku.databinding.ActivityIklanByCategoriBinding
import com.surelabsid.lti.pasaraku.response.DataKategoriItem
import com.surelabsid.lti.pasaraku.response.ResponseListIklan
import com.surelabsid.lti.pasaraku.ui.explore.ExploreViewModel
import com.surelabsid.lti.pasaraku.ui.explore.adapter.AdapterIklan

class IklanByCategoriActivity : AppCompatActivity() {

    private lateinit var adapterIklan: AdapterIklan
    private lateinit var binding: ActivityIklanByCategoriBinding
    private lateinit var vm: ExploreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIklanByCategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this).get(ExploreViewModel::class.java)

        adapterIklan = AdapterIklan(isGrid = false) {
            Intent(this, DetailIklanActivity::class.java).apply {
                putExtra(DetailIklanActivity.DATA_IKLAN, it)
                startActivity(this)
            }
        }

        val kategori = intent.getParcelableExtra<DataKategoriItem>(ITEM_KATEGORI)

        binding.listIklan.apply {
            adapter = adapterIklan
            layoutManager = LinearLayoutManager(this@IklanByCategoriActivity)
        }

        binding.title.text = kategori?.namaKategori

        binding.back.setOnClickListener {
            finish()
        }

        vm.getListIklan(kategori = kategori?.idKategori)
        vm.dataIklan.observe(this) {
            setToView(it)
        }
        vm.error.observe(this) {
            setError(it)
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