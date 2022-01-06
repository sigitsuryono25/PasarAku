package com.surelabsid.lti.pasaraku.ui.register.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentAddEmailBinding
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.utils.Constant
import com.vanillaplacepicker.utils.ToastUtils
import kotlinx.coroutines.*
import retrofit2.HttpException

class AddEmailFragment : Fragment(R.layout.fragment_add_email) {

    private lateinit var binding: FragmentAddEmailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddEmailBinding.bind(view)


        binding.next.setOnClickListener {
            if (binding.emailAnda.text.toString().isNotEmpty()) {
                checkEmail(binding.emailAnda.text.toString())
            } else {
                ToastUtils.showToast(requireActivity(), "email is required")
            }
        }
    }

    private fun checkEmail(email: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val res = NetworkModule.getService().checkedEmail(email)
                    if (res.code == 200) {
                        MainScope().launch {
                            ToastUtils.showToast(requireActivity(), res.message)
                        }
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    when (e) {
                        is HttpException -> {
                            if (e.code() == 404) {
                                MainScope().launch {
                                    Prefs.putString(
                                        Constant.EMAIL,
                                        binding.emailAnda.text.toString()
                                    )
                                    requireActivity().supportFragmentManager.beginTransaction()
                                        .replace(R.id.containerBasicInfo, AddAccountFragment())
                                        .commit()
                                }
                            } else if (e.code() == 500) {
                                MainScope().launch {
                                    ToastUtils.showToast(requireActivity(), e.message())
                                }
                            }
                        }
                        else -> {
                            MainScope().launch {
                                ToastUtils.showToast(requireActivity(), e.message)
                            }
                        }
                    }

                }
            }
        }
    }
}