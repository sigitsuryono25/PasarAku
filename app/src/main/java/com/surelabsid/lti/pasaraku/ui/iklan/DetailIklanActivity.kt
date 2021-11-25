package com.surelabsid.lti.pasaraku.ui.iklan

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ActivityDetailIklanBinding
import com.surelabsid.lti.pasaraku.response.DataIklanItem
import com.surelabsid.lti.pasaraku.utils.Constant
import com.surelabsid.lti.pasaraku.utils.GPSTracker
import java.util.*


class DetailIklanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailIklanBinding
    private var dataIklanItem: DataIklanItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailIklanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataIklanItem = intent.getParcelableExtra(DATA_IKLAN)

        with(binding.mapLokasi) {
            onCreate(savedInstanceState)
            getMapAsync { p0 ->
                MapsInitializer.initialize(this@DetailIklanActivity)
                val latLng = LatLng(
                    dataIklanItem?.lat.toString().toDouble(),
                    dataIklanItem?.lon.toString().toDouble()
                )
                p0.addMarker(
                    MarkerOptions().position(
                        latLng
                    )
                )
                p0.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
            }
        }

        binding.carouselView.pageCount = dataIklanItem?.foto?.size ?: 0
        binding.carouselView.setImageListener { position, imageView ->
            Glide.with(this@DetailIklanActivity)
                .load(
                    Constant.ADS_PIC_URL + dataIklanItem?.iklanId + "/" + dataIklanItem?.foto?.get(
                        position
                    )
                )
                .into(imageView)
        }

        binding.finish.setOnClickListener { finish() }
        binding.harga.text = dataIklanItem?.harga
        binding.detail.text = dataIklanItem?.detail
        binding.description.text = dataIklanItem?.deskripsiIklan
        binding.adsId.text = "ADS ID: ${dataIklanItem?.iklanId}"
        binding.kondisi.text = dataIklanItem?.kondisi
        binding.postOn.text = dataIklanItem?.addedOn

        val location = Location(getString(R.string.app_name))
        location.latitude = dataIklanItem?.lat.toString().toDouble()
        location.longitude = dataIklanItem?.lon.toString().toDouble()

        val listAddress = GPSTracker(this).geocoder(location)
        val kab = listAddress.iterator().next().subAdminArea
        val kec = listAddress.iterator().next().locality
        val prov = listAddress.iterator().next().adminArea

        binding.lokasi.text = "$kec, $kab, $prov"

    }

    override fun onResume() {
        this.binding.mapLokasi.onResume()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        this.binding.mapLokasi.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.binding.mapLokasi.onDestroy()
    }


    companion object {
        const val DATA_IKLAN = "dataIklan"
    }

}