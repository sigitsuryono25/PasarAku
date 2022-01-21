package com.surelabsid.lti.pasaraku

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.base.Baseapp
import com.surelabsid.lti.pasaraku.databinding.ActivityMainBinding
import com.surelabsid.lti.pasaraku.ui.akun.LandingAkunFragment
import com.surelabsid.lti.pasaraku.ui.chat.ChatFragment
import com.surelabsid.lti.pasaraku.ui.explore.ExploreFragment
import com.surelabsid.lti.pasaraku.ui.kategori.KategoriActivity
import com.surelabsid.lti.pasaraku.ui.login.LoginBottomSheet
import com.surelabsid.lti.pasaraku.ui.messages.MessageFragment
import com.surelabsid.lti.pasaraku.ui.myads.MyAdsFragment
import com.surelabsid.lti.pasaraku.utils.Constant
import com.surelabsid.lti.pasaraku.utils.Utils

class MainActivity : Baseapp() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeFragment(ExploreFragment())

        binding.sellBtn.setOnClickListener {
            if (!Prefs.contains(Constant.EMAIL)) {
                Utils.showDialogLogin(supportFragmentManager)
            } else {
                Intent(this@MainActivity, KategoriActivity::class.java).apply {
                    putExtra(KategoriActivity.MAKE_ADS, true)
                    startActivity(this)
                }
            }
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.explore -> {
                    changeFragment(ExploreFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.chat -> {
                    if (!Prefs.contains(Constant.EMAIL)) {
                        Utils.showDialogLogin(supportFragmentManager)
                        return@setOnItemSelectedListener false
                    }
                    changeFragment(MessageFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.ads -> {

                    if (!Prefs.contains(Constant.EMAIL)) {
                        Utils.showDialogLogin(supportFragmentManager)
                        return@setOnItemSelectedListener false
                    }
                    changeFragment(MyAdsFragment())
                    return@setOnItemSelectedListener true

                }
                R.id.account -> {
                    if (!Prefs.contains(Constant.EMAIL)) {
                        Utils.showDialogLogin(supportFragmentManager)
                        return@setOnItemSelectedListener false
                    }
                    changeFragment(LandingAkunFragment())
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }

    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
            .commit()
    }





}