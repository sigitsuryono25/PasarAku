package com.surelabsid.lti.pasaraku.ui.wilayah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.surelabsid.lti.pasaraku.R

class WilayahActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wilayah)

        supportFragmentManager.beginTransaction().replace(R.id.container, ProvinsiFragment())
            .commit()
    }
}