package com.surelabsid.lti.pasaraku.ui.wilayah

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.response.DataKabupatenItem
import com.surelabsid.lti.pasaraku.response.DataProvinsiItem
import com.surelabsid.lti.pasaraku.utils.Constant

class WilayahActivity : AppCompatActivity() {

    private lateinit var wilayahViewModel: WilayahViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wilayah)

        val req = intent.getBooleanExtra(KAB_REQ, false)
        val reqProv = intent.getBooleanExtra(PROVINSI_REQ, false)
        val reqKec = intent.getBooleanExtra(KEC_REQ, false)
        val provinsiItem = intent.getParcelableExtra<DataProvinsiItem?>(PROVINSI_ITEM)
        val kabupatenItem = intent.getParcelableExtra<DataKabupatenItem?>(KAB_ITEM)

        when {
            reqProv -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.container,
                    ProvinsiFragment()
                )
                    .commit()
            }
            req -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.container,
                    WilayahFragment.newInstance(req, provinsiItem?.id, provinsiItem?.nama)
                ).commit()
            }
            reqKec -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.container,
                    WilayahFragment.newInstance(req, kabupatenItem?.id, kabupatenItem?.nama,)
                ).commit()
            }
        }
    }

    companion object {
        const val KAB_REQ = "reqKab"
        const val KEC_REQ = "reqKec"
        const val PROVINSI_ITEM = "provinsItem"
        const val KAB_ITEM = "kabupatenItem"
        const val PROVINSI_REQ = "reqProv"
        const val FROM_SEARCH= "fromSearch"

        const val ISEDIT =  "isedit"
    }
}