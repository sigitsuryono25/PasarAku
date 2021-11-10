package com.surelabsid.lti.pasaraku.ui.wilayah

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.response.DataKabupatenItem
import com.surelabsid.lti.pasaraku.response.DataProvinsiItem

class WilayahActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wilayah)

        val req = intent.getBooleanExtra(KAB_REQ, false)
        val reqProv = intent.getBooleanExtra(PROVINSI_REQ, false)
        val reqKec = intent.getBooleanExtra(KEC_REQ, false)
        val provinsiItem = intent.getParcelableExtra<DataProvinsiItem?>(PROVINSI_ITEM)
        val kabupatenItem = intent.getParcelableExtra<DataKabupatenItem?>(KAB_ITEM)

        if(reqProv){
            supportFragmentManager.beginTransaction().replace(
                R.id.container,
                ProvinsiFragment()
            )
                .commit()
        }else if(req) {
            supportFragmentManager.beginTransaction().replace(
                R.id.container,
                WilayahFragment.newInstance(req, provinsiItem?.id, provinsiItem?.nama)
            )
                .commit()
        }else if(reqKec){
            supportFragmentManager.beginTransaction().replace(
                R.id.container,
                WilayahFragment.newInstance(req, kabupatenItem?.id, kabupatenItem?.nama)
            )
                .commit()
        }
    }

    companion object {
        const val KAB_REQ = "reqKab"
        const val KEC_REQ = "reqKec"
        const val PROVINSI_ITEM = "provinsItem"
        const val KAB_ITEM = "kabupatenItem"
        const val PROVINSI_REQ = "reqProv"
    }
}