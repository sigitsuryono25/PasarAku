package com.surelabsid.lti.pasaraku.utils

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentCurrentLocationAttentionsBinding


class CurrentLocationAttentions : DialogFragment(R.layout.fragment_current_location_attentions) {
    private lateinit var binding: FragmentCurrentLocationAttentionsBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCurrentLocationAttentionsBinding.bind(view)

        binding.attentions.checkboxDontSHow.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Prefs.putBoolean(Constant.DONT_SHOW_ATTENTION_CURRENT_LOCATION, isChecked)
            } else {
                if (Prefs.contains(Constant.DONT_SHOW_ATTENTION_CURRENT_LOCATION))
                    Prefs.remove(Constant.DONT_SHOW_ATTENTION_CURRENT_LOCATION)
            }
        }

        Glide.with(view)
            .load(R.drawable.locatin_3)
            .into(binding.attentions.imageView)

        binding
            .attentions.closed.setOnClickListener {
                dismiss()
            }


    }
}