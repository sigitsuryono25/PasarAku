package com.surelabsid.lti.pasaraku.ui.register.ui

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.MainActivity
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentAdditionalInfoBinding
import com.surelabsid.lti.pasaraku.model.UserRequest
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.GeneralResponse
import com.surelabsid.lti.pasaraku.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdditionalInfoFragment : Fragment(R.layout.fragment_additional_info) {
    private lateinit var binding: FragmentAdditionalInfoBinding
    private lateinit var pd: ProgressDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAdditionalInfoBinding.bind(view)

        binding.next.setOnClickListener {
            if (binding.aboutYou.text.toString().isNotEmpty())
                Prefs.putString(Constant.ABOUT_YOU, binding.aboutYou.text.toString())

            Prefs.putBoolean(Constant.FROM_REGISTER, true)
            val userRequest = UserRequest()
            userRequest.email = Prefs.getString(Constant.EMAIL)
            userRequest.nomor_telepon = Prefs.getString(Constant.PHONE)
            userRequest.password = Prefs.getString(Constant.PASSWORD)
            userRequest.foto = Prefs.getString(Constant.PHOTO)
            userRequest.nama_lengkap = Prefs.getString(Constant.NAME)
            userRequest.facebook_token = Prefs.getString(Constant.FACEBOOK_TOKEN)
            userRequest.google_token = Prefs.getString(Constant.GOOGLE_TOKEN)
            userRequest.tentang_anda = binding.aboutYou.text.toString()

            sendData(userRequest)
        }
    }

    private fun sendData(userRequest: UserRequest) {
        pd = ProgressDialog.show(
            requireActivity(),
            "Silahkan tunggu",
            "mengirimkan data...",
            true,
            false
        )
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val data = NetworkModule.getService().registerUser(userRequest)
                    withContext(Dispatchers.Main) {
                        updateUI(data)
                    }
                } catch (t: Throwable) {
                    t.printStackTrace()
                    withContext(Dispatchers.Main) {
                        showError(t)
                    }
                }
            }
        }
    }

    private fun showError(t: Throwable) {
        pd.dismiss()
        t.printStackTrace()
        Toast.makeText(requireActivity(), t.message.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun updateUI(data: GeneralResponse) {
        pd.dismiss()
        if (data.code == 200) {
            AlertDialog.Builder(requireActivity())
                .setTitle("Informasi")
                .setMessage(data.message)
                .setPositiveButton("Oke") { _, _ ->
                    requireActivity().finishAffinity()
                    Intent(requireActivity(), MainActivity::class.java).apply {
                        startActivity(this)
                    }

                }
                .create().show()
        } else {
            Toast.makeText(requireContext(), data.message, Toast.LENGTH_SHORT).show()
        }
    }
}