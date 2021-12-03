package com.projectlauwba.lautku.firebase.messaging

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.surelabsid.lti.pasaraku.R
import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker
import com.vanillaplacepicker.utils.MapType
import com.vanillaplacepicker.utils.PickerType


class AttachmentListDialogFragment : BottomSheetDialogFragment() {
    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_list_dialog_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.lokasiAnda).setOnClickListener {
            checkGPSPermission()
        }

    }

    private fun checkGPSPermission() {
        val ch = ActivityCompat.checkSelfPermission(
            requireActivity(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (ch == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        } else {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1025
            )
        }
    }

     private fun getLocation() {
        val placePicker = VanillaPlacePicker.Builder(requireActivity())
            .with(PickerType.MAP)
            .setMapType(MapType.NORMAL)
            .build()

        startActivityForResult(placePicker, 2052)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null && requestCode == 2052) {
            val vanillaAddress = VanillaPlacePicker.onActivityResult(data)
            val lat = vanillaAddress?.latitude
            val lon = vanillaAddress?.longitude
            val link = "https://www.google.com/maps?q=$lat,$lon"
            val address = vanillaAddress?.formattedAddress
        }


        dismiss()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1025) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            }
        }
    }

}