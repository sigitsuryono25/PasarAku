package com.surelabsid.lti.pasaraku.ui.wilayah

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.base.Baseapp
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentProvinsiBinding
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.DataProvinsiItem
import com.surelabsid.lti.pasaraku.response.ResponseProvinsi
import com.surelabsid.lti.pasaraku.ui.wilayah.adapter.AdapterWilayah
import com.surelabsid.lti.pasaraku.utils.Constant
import com.surelabsid.lti.pasaraku.utils.GPSTracker
import kotlinx.coroutines.*

private var ISEDIT = "isEdit"

class ProvinsiFragment : Fragment(R.layout.fragment_provinsi) {
    private lateinit var binding: FragmentProvinsiBinding
    private lateinit var vm: WilayahViewModel
    private lateinit var adapterWilayah: AdapterWilayah
    private var listProv: List<DataProvinsiItem?>? = listOf()
    private var isEdit: Boolean = false
    private lateinit var gpsTracker: GPSTracker
    private lateinit var location: Location
    private var lat: Double? = null
    private var lon: Double? = null

    companion object {
        fun newInstance(
            isEdit: Boolean
        ) =
            WilayahFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ISEDIT, isEdit)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isEdit = it.getBoolean(ISEDIT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProvinsiBinding.bind(view)
        vm = ViewModelProvider(this)[WilayahViewModel::class.java]

        gpsTracker = GPSTracker(requireActivity())
        location = gpsTracker.location
        Log.d("onViewCreated", "onViewCreated: ${location.latitude}")

        binding.allProv.setOnClickListener {
            Prefs.remove(Constant.PROV_ID)
            Prefs.remove(Constant.KAB_ID)
            Prefs.remove(Constant.LOKASI_ID)
            Prefs.remove(Constant.KAB)
            Prefs.remove(Constant.KEC)
            Prefs.remove(Constant.PROV)
            requireActivity().finish()
        }

        binding.toolbarWilayah.apply {
            setNavigationOnClickListener {
                requireActivity().finish()
            }
            title = getString(R.string.location)
        }

        binding.useLocation.setOnClickListener {
            (requireActivity() as Baseapp).showLoading()
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    val address = gpsTracker.geocoder(location)
                    if (address.isNotEmpty()) {
                        val a = address.first().postalCode
                        getWilayah(a)
                    }
                }
            }

        }

        adapterWilayah =
            AdapterWilayah(AdapterWilayah.PROVINSI_REQ, object : AdapterWilayah.OnItemClick {
                override fun onProvSelected(dataItemProvinsiItem: DataProvinsiItem?) {
                    super.onProvSelected(dataItemProvinsiItem)
                    Prefs.putString(Constant.PROV_ID, dataItemProvinsiItem?.id)
                    Prefs.putString(Constant.PROV, dataItemProvinsiItem?.nama)
                    Intent(requireActivity(), WilayahActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
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


        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0) {
                    listProv?.let { adapterWilayah.addItem(it, true) }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        binding.search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.search.text.toString().length >= 3) {
                    val filter = listProv?.filter {
                        it?.nama?.contains(binding.search.text.toString(), ignoreCase = true) == true
                    }

//                    Log.d("onViewCreated", "onViewCreated: ${filter?.first()}")

                    filter?.let {
                        adapterWilayah.addItem(it, true)
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Masukkan huruf minimal 3 karakter",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        vm.getProvinsi()
        vm.provinsi.observe(viewLifecycleOwner) {
            setToAdapter(it)
        }
        vm.error.observe(viewLifecycleOwner) {

        }
    }

    private fun setToAdapter(responseProvinsi: ResponseProvinsi) {
        listProv = responseProvinsi.dataProvinsi
        listProv.let {
            if (it != null) {
                adapterWilayah.addItem(it)
            }
        }
    }

    private fun getWilayah(postalCode: String) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val data = NetworkModule.getService().getWilayahByPostalCode(postalCode)
                    MainScope().launch {
                        (requireActivity() as Baseapp).dismissLoading()
                        Prefs.remove(Constant.LOKASI_ID)
                        Prefs.remove(Constant.PROV_ID)
                        Prefs.remove(Constant.PROV)
                        Prefs.remove(Constant.KEC)
                        Prefs.remove(Constant.KAB_ID)
                        Prefs.remove(Constant.KAB)
                        Prefs.putString(Constant.PROV_ID, data.dataWil?.prov?.id)
                        Prefs.putString(Constant.PROV, data.dataWil?.prov?.nama)
                        Prefs.putString(Constant.KAB_ID, data.dataWil?.kab?.id)
                        Prefs.putString(Constant.KAB, data.dataWil?.kab?.nama)
                        requireActivity().finish()
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }
}