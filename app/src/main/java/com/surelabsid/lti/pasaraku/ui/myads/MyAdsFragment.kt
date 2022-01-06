package com.surelabsid.lti.pasaraku.ui.myads

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentMyAdsBinding
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.DataIklanItem
import com.surelabsid.lti.pasaraku.response.ResponseListIklan
import com.surelabsid.lti.pasaraku.ui.iklan.DetailIklanActivity
import com.surelabsid.lti.pasaraku.ui.iklan.TambahIklanActivity
import com.surelabsid.lti.pasaraku.ui.myads.adapter.AdapterMyAds
import com.surelabsid.lti.pasaraku.utils.Constant
import kotlinx.coroutines.*

class MyAdsFragment : Fragment(R.layout.fragment_my_ads) {

    private lateinit var binding: FragmentMyAdsBinding
    private lateinit var adapterIklan: AdapterMyAds

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyAdsBinding.bind(view)

        getMyADs(Prefs.getString(Constant.EMAIL))

        adapterIklan = AdapterMyAds(onRootClick = {
            Intent(requireActivity(), DetailIklanActivity::class.java).apply {
                putExtra(DetailIklanActivity.MY_ADS, true)
                putExtra(DetailIklanActivity.DATA_IKLAN, it)
                startActivity(this)
            }
        }, onPremiumClick = {
            Intent(requireActivity(), PembayaranActivity::class.java).apply {
                putExtra(DetailIklanActivity.DATA_IKLAN, it)
                startActivity(this)
            }
        }, hapusIklan = {
            AlertDialog.Builder(requireActivity())
                .setMessage("Hapus iklan ini?")
                .setTitle("Konfirmasi")
                .setPositiveButton("Ya, Hapus") { d, _ ->
                    d.dismiss()
                    hapusIklan(it)
                }
                .setNegativeButton("Batal") { d, _ ->
                    d.dismiss()
                }
                .create().show()

        }, editIklan = { dataIklanItem ->
            Intent(requireActivity(), TambahIklanActivity::class.java).apply {
                putExtra(TambahIklanActivity.ADS_DATA, dataIklanItem)
                startActivity(this)
            }
        })


        binding.refreshLayout.setOnRefreshListener {
            getMyADs(Prefs.getString(Constant.EMAIL))
        }

        binding.rvIklanSaya.apply {
            adapter = adapterIklan
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }

    }

    private fun hapusIklan(dataIklanItem: DataIklanItem?) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val data = NetworkModule.getService().hapusIklan(dataIklanItem?.iklanId)
                    if (data.code == 200) {
                        MainScope().launch {
                            getMyADs(Prefs.getString(Constant.EMAIL))
                        }
                    } else {
                        MainScope().launch {
                            Toast.makeText(requireActivity(), data.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    MainScope().launch {
                        Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getMyADs(userid: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val myads = NetworkModule.getService().getMyads(userid)
                    withContext(Dispatchers.Main) {
                        updateUI(myads)
                    }
                } catch (e: Throwable) {
                    MainScope().launch {
                        binding.rvIklanSaya.adapter = null
//                        Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
                    }
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateUI(responseListIklan: ResponseListIklan) {
        binding.refreshLayout.isRefreshing = false
        responseListIklan.dataIklan?.let { adapterIklan.addItem(it, true) }
    }

}