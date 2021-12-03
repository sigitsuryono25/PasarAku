package com.surelabsid.lti.pasaraku.ui.register.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentInputNamaBinding
import com.surelabsid.lti.pasaraku.utils.Constant


class NameInputFragment : Fragment(R.layout.fragment_input_nama) {

    private lateinit var binding: FragmentInputNamaBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentInputNamaBinding.bind(view)


        binding.next.setOnClickListener {
            if (binding.namaAnda.text.toString().isEmpty()) {
                Toast.makeText(
                    requireActivity(),
                    "Please fill the column first",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Prefs.putString(Constant.NAME, binding.namaAnda.text.toString())
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.containerBasicInfo, PasswordFragment())
                    .commit()
            }
        }
    }
}