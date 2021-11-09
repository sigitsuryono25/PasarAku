package com.surelabsid.lti.pasaraku.ui.explore

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentExploreBinding
import com.surelabsid.lti.pasaraku.response.ResponseKategori
import com.surelabsid.lti.pasaraku.response.ResponseSlider
import com.surelabsid.lti.pasaraku.ui.explore.adapter.AdapterKategori
import com.surelabsid.lti.pasaraku.ui.kategori.KategoriActivity
import com.surelabsid.lti.pasaraku.ui.wilayah.WilayahActivity
import com.surelabsid.lti.pasaraku.utils.Constant

class ExploreFragment : Fragment(R.layout.fragment_explore) {

    private lateinit var binding: FragmentExploreBinding
    private lateinit var vm: ExploreViewModel

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

        vm.getKategori()
        vm.getSlider()

        adapterKategori = AdapterKategori {

        }
        binding.kategori.apply {
            adapter = adapterKategori
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.wilayah.setOnClickListener {
            Intent(requireActivity(), WilayahActivity::class.java).apply {
                startActivity(this)
            }
        }
        binding.seeAll.setOnClickListener {
            Intent(requireActivity(), KategoriActivity::class.java).apply {
                startActivity(this)
            }
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