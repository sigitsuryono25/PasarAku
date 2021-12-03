package com.surelabsid.lti.pasaraku.ui.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ActivityBasicInfoBinding
import com.surelabsid.lti.pasaraku.ui.register.ui.NameInputFragment

class BasicInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBasicInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBasicInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.containerBasicInfo, NameInputFragment())
            .commit()
    }
}