package com.surelabsid.lti.pasaraku.ui.myads

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentMyAdsBinding
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.ResponseListIklan
import com.surelabsid.lti.pasaraku.ui.explore.adapter.AdapterIklan
import com.surelabsid.lti.pasaraku.ui.iklan.DetailIklanActivity
import com.surelabsid.lti.pasaraku.ui.myads.adapter.AdapterMyAds
import com.surelabsid.lti.pasaraku.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyAdsFragment : Fragment(R.layout.fragment_my_ads) {

    private lateinit var binding: FragmentMyAdsBinding
    private lateinit var adapterIklan: AdapterMyAds

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyAdsBinding.bind(view)

        getMyADs(Prefs.getString(Constant.EMAIL))

        adapterIklan = AdapterMyAds (onRootClick = {
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
        })


        binding.refreshLayout.setOnRefreshListener {
            getMyADs(Prefs.getString(Constant.EMAIL))
        }

        binding.rvIklanSaya.apply {
            adapter = adapterIklan
            layoutManager = GridLayoutManager(requireActivity(), 2)
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