package com.surelabsid.lti.pasaraku.ui.explore

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentExploreBinding
import com.surelabsid.lti.pasaraku.response.ResponseKategori
import com.surelabsid.lti.pasaraku.response.ResponseListIklan
import com.surelabsid.lti.pasaraku.response.ResponseSlider
import com.surelabsid.lti.pasaraku.ui.explore.adapter.AdapterIklan
import com.surelabsid.lti.pasaraku.ui.explore.adapter.AdapterKategori
import com.surelabsid.lti.pasaraku.ui.iklan.DetailIklanActivity
import com.surelabsid.lti.pasaraku.ui.iklan.IklanByCategoriActivity
import com.surelabsid.lti.pasaraku.ui.kategori.KategoriActivity
import com.surelabsid.lti.pasaraku.ui.wilayah.WilayahActivity
import com.surelabsid.lti.pasaraku.utils.Constant

class ExploreFragment : Fragment(R.layout.fragment_explore) {

    private lateinit var binding: FragmentExploreBinding
    private lateinit var vm: ExploreViewModel
    private lateinit var adapterIklan: AdapterIklan

    private lateinit var adapterKategori: AdapterKategori

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExploreBinding.bind(view)

        vm = ViewModelProvider(this).get(ExploreViewModel::class.java)

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
        vm.getListIklan()

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

        adapterIklan = AdapterIklan {
            Intent(requireActivity(), DetailIklanActivity::class.java).apply {
                putExtra(DetailIklanActivity.DATA_IKLAN, it)
                startActivity(this)
            }
        }
        binding.recommendationAds.apply {
            adapter = adapterIklan
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }


        binding.wilayah.setOnClickListener {
            Intent(requireActivity(), WilayahActivity::class.java).apply {
                putExtra(WilayahActivity.PROVINSI_REQ, true)
                startActivity(this)
            }
        }
        binding.seeAll.setOnClickListener {
            Intent(requireActivity(), KategoriActivity::class.java).apply {
                startActivity(this)
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
            } else {
                Prefs.getString(Constant.KAB)
            }
        binding.wilayah.text = lokasi
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