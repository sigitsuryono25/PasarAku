package com.surelabsid.lti.pasaraku.onboard

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.MainActivity
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ActivityOnBoardBinding
import com.surelabsid.lti.pasaraku.utils.Constant

class OnBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnBoardBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.board0.next0.setOnClickListener {
            setView(1)
        }
        binding.board1.next1.setOnClickListener {
            setView(2)
        }
        binding.board2.next2.setOnClickListener {
            setView(3)
        }
        binding.board3.next3.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this@OnBoardActivity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 1024
                )
            } else
                setView(4)
        }
        binding.board4.next4.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this@OnBoardActivity,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 1025
                )
            } else {
                Intent(this@OnBoardActivity, MainActivity::class.java).apply {
                    startActivity(this)
                }
                Prefs.putBoolean(Constant.FIRST_RUN, false)
                finish()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1024) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setView(4)
            }
        } else if (requestCode == 1025) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent(this@OnBoardActivity, MainActivity::class.java).apply {
                    startActivity(this)
                }
                Prefs.putBoolean(Constant.FIRST_RUN, false)
                finish()
            }
        }
    }

    private fun setView(child: Int) {
        binding.flipper.setInAnimation(this@OnBoardActivity, R.anim.from_right)
        binding.flipper.setOutAnimation(this@OnBoardActivity, R.anim.to_left)
        binding.flipper.displayedChild = child
    }
}