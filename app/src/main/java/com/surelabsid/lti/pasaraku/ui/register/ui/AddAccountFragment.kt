package com.surelabsid.lti.pasaraku.ui.register.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentAddAccountBinding


class AddAccountFragment : Fragment(R.layout.fragment_add_account) {

    private lateinit var binding: FragmentAddAccountBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddAccountBinding.bind(view)

        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.containerBasicInfo, AdditionalInfoFragment())
            .commit()

//        binding.addFacebook.setOnClickListener {
//
//        }
//
//        binding.addGoogle.setOnClickListener {
//
//        }
//
//        binding.next.setOnClickListener {
//            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.containerBasicInfo, AdditionalInfoFragment())
//                .commit()
//        }
    }

}