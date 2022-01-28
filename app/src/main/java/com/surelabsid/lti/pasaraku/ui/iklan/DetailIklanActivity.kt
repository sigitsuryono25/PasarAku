package com.surelabsid.lti.pasaraku.ui.iklan

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import com.stfalcon.imageviewer.StfalconImageViewer
import com.surelabsid.lti.base.Baseapp
import com.surelabsid.lti.pasaraku.MainActivity
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ActivityDetailIklanBinding
import com.surelabsid.lti.pasaraku.model.FavoriteRequest
import com.surelabsid.lti.pasaraku.model.firebase.model.ChatHeader
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.DataIklanItem
import com.surelabsid.lti.pasaraku.response.GeneralResponse
import com.surelabsid.lti.pasaraku.response.ResponseListIklan
import com.surelabsid.lti.pasaraku.ui.chat.BottomSheetMessage
import com.surelabsid.lti.pasaraku.ui.explore.ExploreViewModel
import com.surelabsid.lti.pasaraku.ui.explore.adapter.AdapterIklan
import com.surelabsid.lti.pasaraku.ui.iklan.report.ReportDialogFragment
import com.surelabsid.lti.pasaraku.ui.profile.ProfileViewActivity
import com.surelabsid.lti.pasaraku.utils.Constant
import com.surelabsid.lti.pasaraku.utils.Utils
import kotlinx.coroutines.*
import java.util.*
import kotlin.properties.Delegates


class DetailIklanActivity : Baseapp() {
    private lateinit var binding: ActivityDetailIklanBinding
    private var dataIklanItem: DataIklanItem? = null
    private lateinit var adapterIklan: AdapterIklan
    private lateinit var linearLayoutManager: LinearLayoutManager
    var countLoadMore by Delegates.notNull<Int>()
    var isLoading by Delegates.notNull<Boolean>()
    private lateinit var vm: ExploreViewModel
    private var clearAll = true
    private var position = 0
    private var isNotFromScroll = true


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailIklanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this)[ExploreViewModel::class.java]

        vm.dataIklan.observe(this) {
            setIklanToView(it)
        }

        isLoading = false
        countLoadMore = 0

        //from deeplink

        binding.mapLokasi.onCreate(savedInstanceState)



        initAdapter()

        val deepLink: Uri? = intent?.data
        if (deepLink != null) {
            getDetailIklan(deepLink)
            return
        }

        val iklanItem: DataIklanItem? = intent.getParcelableExtra(DATA_IKLAN)
        setDetail(iklanItem)
        binding.finish.setOnClickListener {
            finish()
        }

    }

    private fun setDetail(dataIklanItem: DataIklanItem?) {
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
                getMapAsync { p0 ->
                    MapsInitializer.initialize(this@DetailIklanActivity)
                    val latLng = LatLng(
                        dataIklanItem?.lat.toString().toDouble(),
                        dataIklanItem?.lon.toString().toDouble()
                    )
                    p0.uiSettings.isScrollGesturesEnabled = false
                    p0.addCircle(
                        CircleOptions()
                            .center(latLng)
                            .radius(1000.0)
                            .strokeColor(Color.parseColor("#8100B0FF"))
                            .fillColor(Color.parseColor("#8100B0FF"))
                            .clickable(true)
                    )
                    p0.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                    p0.setOnCircleClickListener {
                        val uri = java.lang.String.format(
                            Locale.ENGLISH,
                            "geo:%f,%f",
                            latLng.latitude,
                            latLng.longitude
                        )
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                            context.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(
                                this@DetailIklanActivity,
                                "No activity can handle this",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
            }
        }

        binding.carouselView.setImageListener { position, imageView ->
            Glide.with(this@DetailIklanActivity)
                .load(
                    Constant.ADS_PIC_URL + dataIklanItem?.iklanId + "/" + dataIklanItem?.foto?.get(
                        position
                    )
                )
                .into(imageView)
        }

        binding.carouselView.pageCount = dataIklanItem?.foto?.size ?: 0

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
        if (dataIklanItem?.idProv?.isEmpty() == true && dataIklanItem.idKab?.isEmpty() == true && dataIklanItem.idKec?.isEmpty() == true) {
            binding.lokasi.text = "Indonesia"
        } else {
            var lokasi = ""
            if (dataIklanItem?.kec?.isNotEmpty() == true) {
                lokasi += "${dataIklanItem.kec}, "
            }
            if (dataIklanItem?.kab?.isNotEmpty() == true) {
                lokasi += "${dataIklanItem.kab}, "
            }
            if (dataIklanItem?.prov?.isNotEmpty() == true) {
                lokasi += "${dataIklanItem.prov}"
            }
            binding.lokasi.text =
                lokasi.trim()

        }

        binding.share.setOnClickListener {
            val url =
                "https://pasaraku.com/mobile/product-detail/${dataIklanItem?.slug}?uuid=${dataIklanItem?.iklanId}"
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, url)
                putExtra(Intent.EXTRA_TITLE, dataIklanItem?.judulIklan)
                type = "text/plain"
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

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

        binding.root.visibility = View.VISIBLE

    }

    private fun getDetailIklan(deepLink: Uri) {
        showLoading()
        val slug = deepLink.pathSegments[deepLink.pathSegments.size - 2]
        val lastPath = deepLink.lastPathSegment

        binding.finish.setOnClickListener {
            finishAffinity()
            Intent(this@DetailIklanActivity, MainActivity::class.java).apply {
                startActivity(this)
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val data = NetworkModule.getServiceNoApi().getDetailIklan(
                        slug = slug,
                        uuid = lastPath
                    )
                    MainScope().launch {
                        dismissLoading()
                        setDetail(data.dataIklan?.first())
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
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

    private fun initAdapter() {
        adapterIklan = AdapterIklan(width = 500, onClick = {
            Intent(this, DetailIklanActivity::class.java).apply {
                putExtra(DATA_IKLAN, it)
                startActivity(this)
            }
        }, onFavClick = { data, img ->
            if (Prefs.contains(Constant.EMAIL)) {
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.IO) {
                        try {
                            val favoriteRequest = FavoriteRequest()
                            favoriteRequest._id_ads = data?.iklanId
                            favoriteRequest._user_id = Prefs.getString(Constant.EMAIL)
                            favoriteRequest.is_add = data?.fav != true
                            val res = NetworkModule.getService().addToFav(favoriteRequest)
                            withContext(Dispatchers.Main) {
                                updateUi(res, img, favoriteRequest)
                            }
                        } catch (e: Throwable) {
                            e.printStackTrace()
                        }
                    }
                }
            } else {
                Utils.showDialogLogin((supportFragmentManager))
            }

        })

        linearLayoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.iklanTerkait.apply {
            adapter = adapterIklan
            layoutManager = linearLayoutManager
        }

        binding.iklanTerkait.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                isNotFromScroll = false
                val lms = recyclerView.layoutManager
                var lastVisiblePosition = 0
                when (lms) {
                    is LinearLayoutManager -> {
                        lastVisiblePosition = lms.findLastCompletelyVisibleItemPosition()
                        position = lms.findFirstVisibleItemPosition()
                    }
                    is GridLayoutManager -> {
                        lastVisiblePosition = lms.findLastCompletelyVisibleItemPosition()
                        position = lms.findFirstVisibleItemPosition()
                    }
                }
                val countItem = lms?.itemCount

                val isLastPosition = countItem?.minus(1) == lastVisiblePosition
                if (!isLoading && isLastPosition) {
                    isLoading = true
                    countLoadMore += 1
                    clearAll = false
                    getIklanTerkait()
                    isLoading = false
                }
            }
        })
    }

    private fun getIklanTerkait() {
        val lokasi =
            if (Prefs.getString(Constant.KEC).isNotEmpty() || Prefs.contains(Constant.KEC)) {
                "${Prefs.getString(Constant.KEC)}, ${Prefs.getString(Constant.KAB)}"
            } else if (Prefs.getString(Constant.KAB).isNotEmpty() || Prefs.contains(Constant.KAB)) {
                Prefs.getString(Constant.KAB)
            } else {
                Prefs.getString(Constant.PROV)
            }
        var user: String? = null
        if (Prefs.contains(Constant.EMAIL)) {
            user = Prefs.getString(Constant.EMAIL)
        }
        if (lokasi.isNotEmpty()) {
            vm.getListIklan(
                page = countLoadMore.toString(),
                provinsi = Prefs.getString(Constant.PROV_ID),
                kabupaten = Prefs.getString(Constant.KAB_ID),
                kecamatan = Prefs.getString(Constant.LOKASI_ID),
                kategori = dataIklanItem?.kategori,
                userid = user
            )
        } else {
            vm.getListIklan(
                userid = user,
                kategori = dataIklanItem?.kategori,
                page = countLoadMore.toString()
            )
        }
    }

    private fun setIklanToView(responseListIklan: ResponseListIklan) {
        val dataIklan = responseListIklan.dataIklan
        binding.iklanTerkait.post {
            dataIklan?.let { adapterIklan.addIklan(it, clearAll) }
        }
    }

    override fun onResume() {
        this.binding.mapLokasi.onResume()
        countLoadMore = 0
        adapterIklan.removeAllItems()
        clearAll = true
        getIklanTerkait()
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