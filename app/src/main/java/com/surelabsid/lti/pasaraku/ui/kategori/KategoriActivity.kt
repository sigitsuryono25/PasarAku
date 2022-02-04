package com.surelabsid.lti.pasaraku.ui.kategori

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.surelabsid.lti.base.Baseapp
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ActivityKategoriBinding
import com.surelabsid.lti.pasaraku.response.ResponseKategori
import com.surelabsid.lti.pasaraku.ui.explore.ExploreViewModel
import com.surelabsid.lti.pasaraku.ui.iklan.IklanByCategoriActivity
import com.surelabsid.lti.pasaraku.ui.iklan.TambahIklanActivity
import com.surelabsid.lti.pasaraku.ui.kategori.adapter.AdapterKategoriVertical

class KategoriActivity : Baseapp() {
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

        val makeAds = intent.getBooleanExtra(MAKE_ADS, false)
        val edit = intent.getStringExtra("edit")

        adapterKategori = AdapterKategoriVertical {
            if (makeAds) {
                Intent(this@KategoriActivity, TambahIklanActivity::class.java).apply {
                    putExtra(TambahIklanActivity.KATEGORI_DATA, it)
                    putExtra("title", "Buat Iklan")
                    startActivity(this)
                    finish()
                }
            } else if (edit?.isNotEmpty() == true) {
                val i = Intent()
                i.putExtra("kategoriItem", it)
                setResult(Activity.RESULT_OK, i)
                finish()
            } else {
                Intent(this@KategoriActivity, IklanByCategoriActivity::class.java).apply {
                    putExtra(IklanByCategoriActivity.ITEM_KATEGORI, it)
                    startActivity(this)
                }
            }
        }
        binding.listKategori.apply {
            adapter = adapterKategori
            layoutManager = LinearLayoutManager(this@KategoriActivity)
        }

        showLoading()

        vm.getKategori()
        vm.kategoriList.observe(this) {
            setToView(it)
        }
        vm.error.observe(this) {
            dismissLoading()
            Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setToView(responseKategori: ResponseKategori) {
        dismissLoading()
        val data = responseKategori.dataKategori
        data.let {
            if (it != null) {
                adapterKategori.addItem(it)
            }
        }
    }

    companion object {
        const val MAKE_ADS = "makeads"
    }
}