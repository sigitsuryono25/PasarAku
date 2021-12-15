package com.surelabsid.lti.pasaraku.ui.wilayah

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentProvinsiBinding
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.DataProvinsiItem
import com.surelabsid.lti.pasaraku.response.ResponseProvinsi
import com.surelabsid.lti.pasaraku.response.ResponseWilayahByPostalCode
import com.surelabsid.lti.pasaraku.ui.wilayah.adapter.AdapterWilayah
import com.surelabsid.lti.pasaraku.utils.Constant
import com.surelabsid.lti.pasaraku.utils.GPSTracker
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ProvinsiFragment : Fragment(R.layout.fragment_provinsi) {

    private lateinit var binding: FragmentProvinsiBinding
    private lateinit var vm: WilayahViewModel
    private lateinit var adapterWilayah: AdapterWilayah

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProvinsiBinding.bind(view)
        vm = ViewModelProvider(this)[WilayahViewModel::class.java]

        binding.allProv.setOnClickListener {
            Prefs.remove(Constant.PROV_ID)
            Prefs.remove(Constant.KAB_ID)
            Prefs.remove(Constant.LOKASI_ID)
            Prefs.remove(Constant.KAB)
            Prefs.remove(Constant.KEC)
            requireActivity().finish()
        }

        binding.toolbarWilayah.apply {
            setNavigationOnClickListener {
                requireActivity().finish()
            }
            title = getString(R.string.location)
        }

        binding.useLocation.setOnClickListener {
            val gpsTracker = GPSTracker(requireActivity())
            val l = Location(getString(R.string.app_name))
                l.latitude = gpsTracker.latitude
                l.longitude = gpsTracker.longitude
            val address = gpsTracker.geocoder(l)
            if(address.isNotEmpty()){
                val a = address.first().postalCode
                getWilayah(a)
            }
        }

        adapterWilayah =
            AdapterWilayah(AdapterWilayah.PROVINSI_REQ, object : AdapterWilayah.OnItemClick {
                override fun onProvSelected(dataItemProvinsiItem: DataProvinsiItem?) {
                    super.onProvSelected(dataItemProvinsiItem)
                    Prefs.putString(Constant.PROV_ID, dataItemProvinsiItem?.id)
                    Prefs.putString(Constant.PROV, dataItemProvinsiItem?.nama)
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

    private fun getWilayah(postalCode: String){
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO){
                try{
                    val data = NetworkModule.getService().getWilayahByPostalCode(postalCode)
                    MainScope().launch {
                        Prefs.putString(Constant.PROV_ID, data.dataWil?.prov?.id)
                        Prefs.putString(Constant.PROV, data.dataWil?.prov?.nama)
                        Prefs.putString(Constant.KAB_ID, data.dataWil?.kab?.id)
                        Prefs.putString(Constant.KAB, data.dataWil?.kab?.nama)
                        requireActivity().finish()
                    }
                }catch(e: Throwable){
                    e.printStackTrace()
                }
            }
        }
    }
}