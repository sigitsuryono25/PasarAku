package com.surelabsid.lti.pasaraku.ui.wilayah

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.MainActivity
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentWilayahBinding
import com.surelabsid.lti.pasaraku.response.DataKabupatenItem
import com.surelabsid.lti.pasaraku.response.DataKecamatanItem
import com.surelabsid.lti.pasaraku.response.ResponseKabupaten
import com.surelabsid.lti.pasaraku.response.ResponseKecamatan
import com.surelabsid.lti.pasaraku.ui.wilayah.adapter.AdapterWilayah
import com.surelabsid.lti.pasaraku.utils.Constant

private const val IS_KAB_REQ = "param1"
private const val ID = "param2"
private const val TITLEBAR = "titlebar"
private const val FROM_SEARCH = "fromSearch"

class WilayahFragment : Fragment(R.layout.fragment_wilayah) {
    private var isKabReq: Boolean? = null
    private var id: String? = null
    private var titleBar: String? = null
    private lateinit var binding: FragmentWilayahBinding
    private lateinit var vm: WilayahViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isKabReq = it.getBoolean(IS_KAB_REQ)
            id = it.getString(ID)
            titleBar = it.getString(TITLEBAR)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWilayahBinding.bind(view)
        vm = ViewModelProvider(requireActivity())[WilayahViewModel::class.java]

        binding.toolbarWilayah.apply {
            title = titleBar
            navigationIcon = ActivityCompat.getDrawable(requireActivity(), R.drawable.arrow_left)
            setNavigationOnClickListener {
                requireActivity().finish()
            }
        }

        vm.error.observe(viewLifecycleOwner) {
            setError(it)
        }

        binding.selectedLok.text = "All in $titleBar"


        if (isKabReq == true) {
            vm.getKabupaten(id)
            vm.kabupaten.observe(viewLifecycleOwner) {
                setKabupaten(it)
            }
        } else {
            vm.getKecamatan(id)
            vm.kecamatan.observe(viewLifecycleOwner) {
                setKecamatan(it)
            }
        }

    }

    private fun setKabupaten(responseKabupaten: ResponseKabupaten) {
        binding.selectedLok.setOnClickListener {
            Prefs.remove(Constant.KEC)
            Prefs.remove(Constant.LOKASI_ID)
            Prefs.putString(Constant.PROV, titleBar)
            Prefs.putString(Constant.PROV_ID, id)
            Prefs.remove(Constant.KAB)
            Prefs.remove(Constant.KAB_ID)

            requireActivity().finishAffinity()
            startActivity(Intent(requireActivity(), MainActivity::class.java))
        }
        val adapterKabupaten =
            AdapterWilayah(AdapterWilayah.KABUPATEN_REQ, object : AdapterWilayah.OnItemClick {
                override fun onKabSelected(dataItemKabupatenItem: DataKabupatenItem?) {
                    super.onKabSelected(dataItemKabupatenItem)
                    Prefs.putString(Constant.KAB, dataItemKabupatenItem?.nama)
                    Prefs.putString(Constant.KAB_ID, dataItemKabupatenItem?.id)
                    Intent(requireActivity(), WilayahActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        putExtra(WilayahActivity.KEC_REQ, true)
                        putExtra(WilayahActivity.KAB_ITEM, dataItemKabupatenItem)
                        startActivity(this)
                    }
                }
            })

        binding.wilayah.apply {
            adapter = adapterKabupaten
            layoutManager = LinearLayoutManager(requireActivity())
        }
        val data = responseKabupaten.dataKabupaten
        data.let {
            if (it != null) {
                adapterKabupaten.addItemKab(it)
            }
        }
    }

    private fun setKecamatan(responseKecamatan: ResponseKecamatan) {
        binding.selectedLok.setOnClickListener {
            Prefs.putString(Constant.KAB, titleBar)
            Prefs.putString(Constant.KAB_ID, id)

            Prefs.remove(Constant.KEC)
            Prefs.remove(Constant.LOKASI_ID)
            requireActivity().finishAffinity()
            startActivity(Intent(requireActivity(), MainActivity::class.java))
        }
        val adapterKecamatan =
            AdapterWilayah(AdapterWilayah.KECAMATAN_REQ, object : AdapterWilayah.OnItemClick {
                override fun onKecSelected(dataKecamatanItem: DataKecamatanItem?) {
                    super.onKecSelected(dataKecamatanItem)
                    Prefs.putString(Constant.KEC, dataKecamatanItem?.nama)
                    Prefs.putString(Constant.LOKASI_ID, dataKecamatanItem?.id)

//                    if (Prefs.getBoolean(Constant.FROM_SEARCH)) {
                        requireActivity().finish()
//                    } else {
//                        requireActivity().finishAffinity()
//                        startActivity(Intent(requireActivity(), MainActivity::class.java))
//                    }
                }
            })

        binding.wilayah.apply {
            adapter = adapterKecamatan
            layoutManager = LinearLayoutManager(requireActivity())
        }
        val data = responseKecamatan.dataKecamatan
        data.let {
            if (it != null) {
                adapterKecamatan.addItemKec(it)
            }
        }
    }

    private fun setError(it: Throwable?) {
        Toast.makeText(requireActivity(), it?.message.toString(), Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance(
            isKabReq: Boolean,
            id: String?,
            title: String?
        ) =
            WilayahFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_KAB_REQ, isKabReq)
                    putString(ID, id)
                    putString(TITLEBAR, title)
                }
            }
    }
}