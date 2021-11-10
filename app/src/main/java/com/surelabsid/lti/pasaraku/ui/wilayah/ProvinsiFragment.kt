package com.surelabsid.lti.pasaraku.ui.wilayah

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentProvinsiBinding
import com.surelabsid.lti.pasaraku.response.DataProvinsiItem
import com.surelabsid.lti.pasaraku.response.ResponseProvinsi
import com.surelabsid.lti.pasaraku.ui.wilayah.adapter.AdapterWilayah
import com.surelabsid.lti.pasaraku.utils.Constant

class ProvinsiFragment : Fragment(R.layout.fragment_provinsi) {

    private lateinit var binding: FragmentProvinsiBinding
    private lateinit var vm: WilayahViewModel
    private lateinit var adapterWilayah: AdapterWilayah

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProvinsiBinding.bind(view)
        vm = ViewModelProvider(this)[WilayahViewModel::class.java]

        binding.allProv.setOnClickListener {
            Prefs.remove(Constant.KEC)
            Prefs.putString(Constant.KAB, "Indonesia")
            Prefs.putString(Constant.LOKASI_ID, "ID")
            requireActivity().finish()
        }

        binding.toolbarWilayah.apply {
            setNavigationOnClickListener {
                requireActivity().finish()
            }
            title = getString(R.string.location)
        }

        adapterWilayah =
            AdapterWilayah(AdapterWilayah.PROVINSI_REQ, object : AdapterWilayah.OnItemClick {
                override fun onProvSelected(dataItemProvinsiItem: DataProvinsiItem?) {
                    super.onProvSelected(dataItemProvinsiItem)
                    Intent(requireActivity(), WilayahActivity::class.java).apply {
                        putExtra(WilayahActivity.KAB_REQ, true)
                        putExtra(WilayahActivity.PROVINSI_ITEM, dataItemProvinsiItem)
                        startActivity(this)
                    }
                }
            })
        binding.allProvince.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = adapterWilayah
        }

        vm.getProvinsi()
        vm.provinsi.observe(viewLifecycleOwner) {
            setToAdapter(it)
        }
        vm.error.observe(viewLifecycleOwner) {

        }
    }

    private fun setToAdapter(responseProvinsi: ResponseProvinsi) {
        val data = responseProvinsi.dataProvinsi
        data.let {
            if (it != null) {
                adapterWilayah.addItem(it)
            }
        }
    }
}