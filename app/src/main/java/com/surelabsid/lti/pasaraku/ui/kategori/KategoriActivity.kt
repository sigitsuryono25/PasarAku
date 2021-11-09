package com.surelabsid.lti.pasaraku.ui.kategori

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ActivityKategoriBinding
import com.surelabsid.lti.pasaraku.response.ResponseKategori
import com.surelabsid.lti.pasaraku.ui.explore.ExploreViewModel
import com.surelabsid.lti.pasaraku.ui.kategori.adapter.AdapterKategoriVertical

class KategoriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKategoriBinding
    private lateinit var vm: ExploreViewModel
    private lateinit var adapterKategori: AdapterKategoriVertical

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarKategori)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.categories)
        }

        vm = ViewModelProvider(this)[ExploreViewModel::class.java]

        adapterKategori = AdapterKategoriVertical { }
        binding.listKategori.apply {
            adapter = adapterKategori
            layoutManager = LinearLayoutManager(this@KategoriActivity)
        }

        vm.getKategori()
        vm.kategoriList.observe(this) {
            setToView(it)
        }
        vm.error.observe(this) {

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setToView(responseKategori: ResponseKategori) {
        val data = responseKategori.dataKategori
        data.let {
            if (it != null) {
                adapterKategori.addItem(it)
            }
        }
    }
}