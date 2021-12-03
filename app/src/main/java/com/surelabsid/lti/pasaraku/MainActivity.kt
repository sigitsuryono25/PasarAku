package com.surelabsid.lti.pasaraku

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.databinding.ActivityMainBinding
import com.surelabsid.lti.pasaraku.ui.akun.LandingAkunFragment
import com.surelabsid.lti.pasaraku.ui.chat.ChatFragment
import com.surelabsid.lti.pasaraku.ui.explore.ExploreFragment
import com.surelabsid.lti.pasaraku.ui.kategori.KategoriActivity
import com.surelabsid.lti.pasaraku.ui.login.LoginBottomSheet
import com.surelabsid.lti.pasaraku.utils.Constant

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeFragment(ExploreFragment())

        binding.sellBtn.setOnClickListener {
            Intent(this@MainActivity, KategoriActivity::class.java).apply {
                putExtra(KategoriActivity.MAKE_ADS, true)
                startActivity(this)
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
                        showDialogLogin()
                        return@setOnItemSelectedListener false
                    }
                    changeFragment(ChatFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.ads -> {

                    if (!Prefs.contains(Constant.EMAIL)) {
                        showDialogLogin()
                        return@setOnItemSelectedListener false
                    }

                    return@setOnItemSelectedListener true

                }
                R.id.account -> {
                    if (!Prefs.contains(Constant.EMAIL)) {
                        showDialogLogin()
                        return@setOnItemSelectedListener false
                    }
                    changeFragment(LandingAkunFragment())
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }

        checkPermission()
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
            .commit()
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ), 200
            )
        }
    }


    private fun showDialogLogin() {
        val loginDialog = LoginBottomSheet()
        loginDialog.show(supportFragmentManager, "dialogLogin")
    }
}