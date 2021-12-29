package com.surelabsid.lti.pasaraku.ui.explore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.MainActivity
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentExploreBinding
import com.surelabsid.lti.pasaraku.model.FavoriteRequest
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.GeneralResponse
import com.surelabsid.lti.pasaraku.response.ResponseKategori
import com.surelabsid.lti.pasaraku.response.ResponseListIklan
import com.surelabsid.lti.pasaraku.response.ResponseSlider
import com.surelabsid.lti.pasaraku.ui.explore.adapter.AdapterIklan
import com.surelabsid.lti.pasaraku.ui.explore.adapter.AdapterKategori
import com.surelabsid.lti.pasaraku.ui.iklan.DetailIklanActivity
import com.surelabsid.lti.pasaraku.ui.iklan.IklanByCategoriActivity
import com.surelabsid.lti.pasaraku.ui.iklan.PencarianActivity
import com.surelabsid.lti.pasaraku.ui.kategori.KategoriActivity
import com.surelabsid.lti.pasaraku.ui.notification.NotificationActivity
import com.surelabsid.lti.pasaraku.ui.wilayah.WilayahActivity
import com.surelabsid.lti.pasaraku.utils.Constant
import com.surelabsid.lti.pasaraku.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExploreFragment : Fragment(R.layout.fragment_explore) {

    private lateinit var binding: FragmentExploreBinding
    private lateinit var vm: ExploreViewModel
    private lateinit var adapterIklan: AdapterIklan

    private lateinit var adapterKategori: AdapterKategori

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExploreBinding.bind(view)

        vm = ViewModelProvider(this)[ExploreViewModel::class.java]

        vm.kategoriList.observe(viewLifecycleOwner) {
            setToKategori(it)
        }
        vm.slider.observe(viewLifecycleOwner) {
            setSlider(it)
        }

        vm.error.observe(viewLifecycleOwner) {

        }

        vm.dataIklan.observe(viewLifecycleOwner) {
            setIklanToView(it)
        }

        vm.getKategori()
        vm.getSlider()

        adapterKategori = AdapterKategori {
            Intent(requireActivity(), IklanByCategoriActivity::class.java).apply {
                putExtra(IklanByCategoriActivity.ITEM_KATEGORI, it)
                startActivity(this)
            }
        }
        binding.kategori.apply {
            adapter = adapterKategori
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }

        adapterIklan = AdapterIklan(onClick = {
            Intent(requireActivity(), DetailIklanActivity::class.java).apply {
                putExtra(DetailIklanActivity.DATA_IKLAN, it)
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
            }else{
                Utils.showDialogLogin((requireActivity().supportFragmentManager))
            }

        })
        binding.recommendationAds.apply {
            adapter = adapterIklan
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }

        binding.notif.setOnClickListener {
            Intent(requireActivity(), NotificationActivity::class.java).apply {
                startActivity(this)
            }
        }


        binding.wilayah.setOnClickListener {
            Intent(requireActivity(), WilayahActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra(WilayahActivity.PROVINSI_REQ, true)
                startActivity(this)
            }
        }
        binding.seeAll.setOnClickListener {
            Intent(requireActivity(), KategoriActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                Intent(requireActivity(), PencarianActivity::class.java).apply {
                    putExtra(PencarianActivity.SEARCH_DATA, binding.search.text.toString())
                    startActivity(this)
                }
            }
            false
        }

    }

    private fun updateUi(res: GeneralResponse, img: ImageView, favoriteRequest: FavoriteRequest) {
        val gson = Gson()
        Log.d("updateUi", "updateUi: "+ gson.toJson(favoriteRequest))
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
                Toast.makeText(requireActivity(), res.message, Toast.LENGTH_SHORT).show()
                onResume()
            }
            else -> {
                Toast.makeText(requireActivity(), res.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setIklanToView(responseListIklan: ResponseListIklan) {
        val dataIklan = responseListIklan.dataIklan
        dataIklan?.let { adapterIklan.addIklan(it, true) }
    }

    override fun onResume() {
        super.onResume()
        val lokasi =
            if (Prefs.getString(Constant.KEC).isNotEmpty() || Prefs.contains(Constant.KEC)) {
                "${Prefs.getString(Constant.KEC)}, ${Prefs.getString(Constant.KAB)}"
            } else if (Prefs.getString(Constant.KAB).isNotEmpty() || Prefs.contains(Constant.KAB)) {
                Prefs.getString(Constant.KAB)
            }else{
                Prefs.getString(Constant.PROV)
            }
        var user: String? = null
        if (Prefs.contains(Constant.EMAIL)) {
            user = Prefs.getString(Constant.EMAIL)
        }
        if (lokasi.isNotEmpty()) {
            binding.wilayah.text = lokasi

            vm.getListIklan(
                provinsi = Prefs.getString(Constant.PROV_ID),
                kabupaten = Prefs.getString(Constant.KAB_ID),
                kecamatan = Prefs.getString(Constant.LOKASI_ID),
                userid = user
            )
        } else {
            binding.wilayah.text = "Indonesia"
            vm.getListIklan(userid = user)
        }
    }

    private fun setSlider(responseSlider: ResponseSlider) {
        binding.progress.visibility = View.GONE
        binding.carouselView.visibility = View.VISIBLE
        val dataSlider = responseSlider.dataSlider
        val path = Constant.SLIDER_COVER
        binding.carouselView.setImageListener { position, imageView ->
            Glide.with(requireActivity())
                .load(path + dataSlider?.get(position)?.cover)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(imageView)
        }
        binding.carouselView.pageCount = dataSlider?.size ?: 0
    }

    private fun setToKategori(responseKategori: ResponseKategori) {
        val data = responseKategori.dataKategori
        data.let {
            if (it != null) {
                adapterKategori.additem(it)
            }
        }
    }

}