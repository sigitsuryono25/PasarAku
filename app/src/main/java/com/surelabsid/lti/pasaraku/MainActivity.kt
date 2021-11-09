package com.surelabsid.lti.pasaraku

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.surelabsid.lti.pasaraku.ui.explore.ExploreFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ExploreFragment())
            .commit()
    }
}