package com.surelabsid.lti.test

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.surelabsid.lti.pasaraku.R

class DeepLinkTest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deep_link_test)

        val action: String? = intent?.action

//        val id = data?.lastPathSegment
//        Toast.makeText(this, "$id", Toast.LENGTH_SHORT).show()
    }
}