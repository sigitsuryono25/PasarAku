package com.surelabsid.lti.pasaraku.ui.register.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentPasswordBinding
import com.surelabsid.lti.pasaraku.utils.Constant


class PasswordFragment : Fragment(R.layout.fragment_password) {

    private lateinit var binding : FragmentPasswordBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPasswordBinding.bind(view)

        binding.next.setOnClickListener {
            if(binding.password.text.toString().isEmpty()){
                Toast.makeText(requireActivity(), "Please fill password first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(binding.confirmPassword.text.toString().isEmpty()){
                Toast.makeText(
                    requireActivity(),
                    "Please fill confirm password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if(!binding.password.text.toString().equals(binding.confirmPassword.text.toString())){
                Toast.makeText(requireActivity(), "Your password is not match. Please check it again", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Prefs.putString(Constant.PASSWORD, binding.password.text.toString())
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.containerBasicInfo, AddPictureProfileFragment())
                .commit()
        }
    }

}
