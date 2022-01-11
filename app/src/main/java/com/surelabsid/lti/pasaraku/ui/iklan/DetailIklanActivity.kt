package com.surelabsid.lti.pasaraku.ui.iklan

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import com.stfalcon.imageviewer.StfalconImageViewer
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ActivityDetailIklanBinding
import com.surelabsid.lti.pasaraku.model.FavoriteRequest
import com.surelabsid.lti.pasaraku.model.firebase.model.ChatHeader
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.DataIklanItem
import com.surelabsid.lti.pasaraku.response.GeneralResponse
import com.surelabsid.lti.pasaraku.ui.chat.BottomSheetMessage
import com.surelabsid.lti.pasaraku.ui.iklan.report.ReportDialogFragment
import com.surelabsid.lti.pasaraku.ui.profile.ProfileViewActivity
import com.surelabsid.lti.pasaraku.utils.Constant
import com.surelabsid.lti.pasaraku.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import com.google.android.gms.maps.model.CircleOptions

import com.google.android.gms.maps.model.Circle





class DetailIklanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailIklanBinding
    private var dataIklanItem: DataIklanItem? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailIklanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataIklanItem = intent.getParcelableExtra(DATA_IKLAN)
        Log.d("onCreate", "onCreate: $dataIklanItem")
        val isMyAds = intent.getBooleanExtra(MY_ADS, false)
        if (isMyAds) {
            binding.bottomBar.visibility = View.GONE
        }
        if (dataIklanItem?.lat?.isEmpty() == true || dataIklanItem?.lon?.isEmpty() == true) {
            binding.mapLokasi.visibility = View.GONE
            binding.noLokasi.visibility = View.VISIBLE
        } else {
            with(binding.mapLokasi) {
                onCreate(savedInstanceState)
                getMapAsync { p0 ->
                    MapsInitializer.initialize(this@DetailIklanActivity)
                    val latLng = LatLng(
                        dataIklanItem?.lat.toString().toDouble(),
                        dataIklanItem?.lon.toString().toDouble()
                    )
//                    p0.addMarker(
//                        MarkerOptions().position(
//                            latLng
//                        )
//                    )
                    p0.uiSettings.isScrollGesturesEnabled = false
                    p0.addCircle(
                        CircleOptions()
                            .center(latLng)
                            .radius(1000.0)
                            .strokeColor(Color.parseColor("#8100B0FF"))
                            .fillColor(Color.parseColor("#8100B0FF"))
                    )
                    p0.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                }
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

        binding.carouselView.setImageClickListener {
            StfalconImageViewer.Builder<String>(
                this@DetailIklanActivity,
                dataIklanItem?.foto
            ) { imageView, image ->
                Glide.with(this)
                    .load(Constant.ADS_PIC_URL + dataIklanItem?.iklanId + "/" + image)
                    .into(imageView)
            }.withStartPosition(it).show(true)
        }

        binding.finish.setOnClickListener { finish() }
        binding.harga.text = dataIklanItem?.harga
        binding.detail.text = dataIklanItem?.detail
        binding.description.text = dataIklanItem?.deskripsiIklan
        binding.adsId.text = "ADS ID: ${dataIklanItem?.iklanId}"
        binding.kondisi.text = dataIklanItem?.kondisi
        binding.postOn.text = dataIklanItem?.addedOn

        binding.judulIklan.text = dataIklanItem?.judulIklan

        binding.namaUser.text = dataIklanItem?.namaLengkap
        Glide.with(this)
            .load(NetworkModule.BASE_URL + Constant.URL_IMAGE_USER + dataIklanItem?.profilePic)
            .into(binding.profilePic)
        binding.terdaftarSejak.text = dataIklanItem?.bergabung


        val location = Location(getString(R.string.app_name))
        location.latitude = dataIklanItem?.lat.toString().toDouble()
        location.longitude = dataIklanItem?.lon.toString().toDouble()
        if (dataIklanItem?.idProv?.isEmpty() == true && dataIklanItem?.idKab?.isEmpty() == true && dataIklanItem?.idKec?.isEmpty() == true) {
            binding.lokasi.text = "Indonesia"
        } else {
            var lokasi = ""
            if (dataIklanItem?.kec?.isNotEmpty() == true) {
                lokasi += "${dataIklanItem?.kec}, "
            }
            if (dataIklanItem?.kab?.isNotEmpty() == true) {
                lokasi += "${dataIklanItem?.kab}, "
            }
            if (dataIklanItem?.prov?.isNotEmpty() == true) {
                lokasi += "${dataIklanItem?.prov}"
            }
            binding.lokasi.text =
                lokasi.trim()

        }

//        val listAddress = GPSTracker(this).geocoder(location)
//        if (listAddress.isNotEmpty()) {
//            val kab = listAddress.iterator().next().subAdminArea
//            val kec = listAddress.iterator().next().locality
//            val prov = listAddress.iterator().next().adminArea
//
//            binding.lokasi.text = "$kec, $kab, $prov"
//        } else {
//            binding.lokasi.text = "Lokasi tidak diketahui"
//        }

        binding.wa.setOnClickListener {
            val message = "Halo, saya mau tanya tentang iklan ${dataIklanItem?.judulIklan}"
            val telp = dataIklanItem?.nomorTelepon?.replace("+", "")

            Utils.openWhatsApp(context = this, numero = telp.toString(), mensaje = message)
        }

        binding.chat.setOnClickListener {
            val chatHeader = ChatHeader()
            chatHeader._id = System.currentTimeMillis()
            chatHeader.added_by = Prefs.getString(Constant.EMAIL)
            chatHeader.user_id = dataIklanItem?.email
            chatHeader.nama = dataIklanItem?.namaLengkap
            chatHeader.token = dataIklanItem?.token
            chatHeader._id_ads = dataIklanItem?.iklanId


            val dialog = BottomSheetMessage.newInstance(chatHeader)
            dialog.show(supportFragmentManager, "dialog_chat")
        }

        binding.call.setOnClickListener {
            val callDialer = Intent(Intent.ACTION_VIEW)
            callDialer.data = Uri.parse("tel:${dataIklanItem?.nomorTelepon}")
            startActivity(callDialer)
        }

        binding.report.setOnClickListener {
            val dialog = ReportDialogFragment.newInstance(dataIklanItem?.iklanId)
            dialog.show(supportFragmentManager, "report")
        }

        if (dataIklanItem?.fav == true) {
            binding.fav.setColorFilter(Color.parseColor("#F44336"))
            Glide.with(this)
                .load(R.drawable.ic_baseline_favorite)
                .into(binding.fav)
        } else {
            binding.fav.setColorFilter(Color.parseColor("#FF000000"))
            Glide.with(this)
                .load(R.drawable.ic_fav)
                .into(binding.fav)
        }

        binding.profileView.setOnClickListener {
            Intent(this@DetailIklanActivity, ProfileViewActivity::class.java).apply {
                putExtra(ProfileViewActivity.USERID, dataIklanItem?.addedBy)
                startActivity(this)
            }
        }

        binding.fav.setOnClickListener {
            if (Prefs.contains(Constant.EMAIL)) {
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.IO) {
                        try {
                            val favoriteRequest = FavoriteRequest()
                            favoriteRequest._id_ads = dataIklanItem?.iklanId
                            favoriteRequest._user_id = Prefs.getString(Constant.EMAIL)
                            favoriteRequest.is_add = dataIklanItem?.fav != true
                            val res = NetworkModule.getService().addToFav(favoriteRequest)
                            withContext(Dispatchers.Main) {
                                updateUi(res, binding.fav, favoriteRequest)
                            }
                        } catch (e: Throwable) {
                            e.printStackTrace()
                        }
                    }
                }
            } else {
                Utils.showDialogLogin(supportFragmentManager)
            }
        }

    }

    private fun updateUi(res: GeneralResponse, img: ImageView, favoriteRequest: FavoriteRequest) {
        val gson = Gson()
        Log.d("updateUi", "updateUi: " + gson.toJson(favoriteRequest))
        when (res.code) {
            200 -> {
                if (favoriteRequest.is_add) {
                    Glide.with(this)
                        .load(R.drawable.ic_baseline_favorite)
                        .into(img)
                } else {
                    Glide.with(this)
                        .load(R.drawable.ic_fav)
                        .into(img)
                }
                Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
            }
        }
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
        const val MY_ADS = "myAds"
    }

}