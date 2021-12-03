package com.surelabsid.lti.pasaraku.ui.register.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentAddEmailBinding
import com.surelabsid.lti.pasaraku.utils.Constant

class AddEmailFragment : Fragment(R.layout.fragment_add_email) {

    private lateinit var binding: FragmentAddEmailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddEmailBinding.bind(view)


        binding.next.setOnClickListener {
            if (binding.emailAnda.text.toString().isNotEmpty())
                Prefs.putString(Constant.EMAIL, binding.emailAnda.text.toString())


            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.containerBasicInfo, AddAccountFragment())
                .commit()
        }
    }
}