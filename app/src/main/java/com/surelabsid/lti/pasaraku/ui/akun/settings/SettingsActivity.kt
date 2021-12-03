package com.surelabsid.lti.pasaraku.ui.akun.settings

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.MainActivity
import com.surelabsid.lti.pasaraku.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Settings"
            setDisplayHomeAsUpEnabled(true)
        }

        binding.logout.setOnClickListener {
            AlertDialog.Builder(this@SettingsActivity)
                .setMessage("Keluar dari aplikasi?")
                .setTitle("Konfirmasi")
                .setPositiveButton("Ya, Keluar") { _, _ ->
                    Prefs.clear()
                    finishAffinity()
                    startActivity(Intent(this@SettingsActivity, MainActivity::class.java))
                }.setNegativeButton("Batal") { d, _ ->
                    d.dismiss()
                }.create().show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}