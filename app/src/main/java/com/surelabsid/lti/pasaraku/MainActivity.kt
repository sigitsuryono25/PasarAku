package com.surelabsid.lti.pasaraku

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.databinding.ActivityMainBinding
import com.surelabsid.lti.pasaraku.ui.explore.ExploreFragment
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

        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.explore -> {
                    changeFragment(ExploreFragment())
                }
                R.id.chat -> {
                    if (!Prefs.contains(Constant.EMAIL)) {
                        showDialogLogin()
                        return@setOnItemSelectedListener true
                    }

                    return@setOnItemSelectedListener true
                }
                R.id.ads -> {
                    if (!Prefs.contains(Constant.EMAIL)) {
                        showDialogLogin()
                        return@setOnItemSelectedListener true
                    }

                    return@setOnItemSelectedListener true

                }
                R.id.account -> {
                    if (!Prefs.contains(Constant.EMAIL)) {
                        showDialogLogin()
                        return@setOnItemSelectedListener true
                    }

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

    private fun showDialogLogin() {
        val loginDialog = LoginBottomSheet()
        loginDialog.show(supportFragmentManager, "dialogLogin")
    }
}