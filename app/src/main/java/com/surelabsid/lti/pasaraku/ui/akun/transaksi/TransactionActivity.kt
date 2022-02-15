package com.surelabsid.lti.pasaraku.ui.akun.transaksi

import android.os.Bundle
import android.view.MenuItem
import com.surelabsid.lti.base.Baseapp
import com.surelabsid.lti.pasaraku.R

class TransactionActivity : Baseapp() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        supportActionBar?.apply {
            title = "Your Transaction"
            setDisplayHomeAsUpEnabled(true)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerTransaksi, TransaksiFragment.newInstance(false))
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}