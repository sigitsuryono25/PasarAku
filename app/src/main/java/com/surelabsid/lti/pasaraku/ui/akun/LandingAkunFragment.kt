package com.surelabsid.lti.pasaraku.ui.akun

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentLandingAkunBinding
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.ui.akun.settings.SettingsActivity
import com.surelabsid.lti.pasaraku.ui.favorite.FavoriteActivity
import com.surelabsid.lti.pasaraku.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class LandingAkunFragment : Fragment(R.layout.fragment_landing_akun) {
    private lateinit var binding: FragmentLandingAkunBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLandingAkunBinding.bind(view)
        binding.namaLengkap.text = Prefs.getString(Constant.NAME)

        if (Prefs.getBoolean(Constant.FROM_REGISTER)) {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    val decodeString =
                        Base64.decode(Prefs.getString(Constant.PHOTO), Base64.DEFAULT)
                    val bmp = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.size)
                    withContext(Dispatchers.Main) {
                        Glide.with(this@LandingAkunFragment)
                            .asBitmap()
                            .load(bmp)
                            .into(binding.imageUser)
                    }

                }
            }
        } else {
            checkNumber(Prefs.getString(Constant.PHONE))
        }
        binding.settings.setOnClickListener {
            Intent(requireActivity(), SettingsActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.favorite.setOnClickListener {
            Intent(requireActivity(), FavoriteActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun checkNumber(cleanNumber: String) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val checkPhoneNumber = NetworkModule.getService().checkPhone("$cleanNumber")
                    if (checkPhoneNumber.code == 200) {
                        withContext(Dispatchers.Main) {
                            Glide.with(this@LandingAkunFragment)
                                .asBitmap()
                                .load(NetworkModule.BASE_URL + Constant.URL_IMAGE_USER + checkPhoneNumber.dataUser?.foto)
                                .into(binding.imageUser)
                        }
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()

                }

            }
        }
    }
}