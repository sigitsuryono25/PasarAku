package com.surelabsid.lti.pasaraku.ui.akun.transaksi

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.base.Baseapp
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ActivityTransaksiBinding
import com.surelabsid.lti.pasaraku.model.PremiumModel
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.ResponseTransaksi
import com.surelabsid.lti.pasaraku.ui.WebViewActivity
import com.surelabsid.lti.pasaraku.ui.akun.transaksi.adapter.AdapterTransaksi
import com.surelabsid.lti.pasaraku.utils.Constant
import com.vanillaplacepicker.utils.ToastUtils
import kotlinx.coroutines.*


private const val REQ_TOOLBAR = "param1"

class TransaksiFragment : Fragment(R.layout.activity_transaksi) {
    private lateinit var binding: ActivityTransaksiBinding
    private lateinit var mAdapterTransaksi: AdapterTransaksi
    private var reqToolbar: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            reqToolbar = it.getBoolean(REQ_TOOLBAR)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ActivityTransaksiBinding.bind(view)

        if (reqToolbar == false) {
            binding.toolbarTransaksi.visibility = View.GONE
        }

        val userid = Prefs.getString(Constant.EMAIL)
        getTrans(userid)

        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = false
            getTrans(userid)
        }

        mAdapterTransaksi = AdapterTransaksi {
            val premiumModel = PremiumModel()
            premiumModel.id = it?.id
            Intent(requireActivity(), WebViewActivity::class.java).apply {
                putExtra(WebViewActivity.REKG, premiumModel)
                startActivity(this)
            }
        }

        binding.rvTrans.apply {
            adapter = mAdapterTransaksi
            addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    LinearLayoutManager.VERTICAL
                )
            )
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        }
    }


    private fun getTrans(userid: String?) {
        (requireActivity() as Baseapp).showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val resp = NetworkModule.getService().getTransaksi(userid)
                    MainScope().launch {
                        (requireActivity() as Baseapp).dismissLoading()
                        updateUI(resp)
                    }
                } catch (e: Throwable) {
                    MainScope().launch {
                        (requireActivity() as Baseapp).dismissLoading()
                        ToastUtils.showToast(requireActivity(), e.message)
                    }
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateUI(resp: ResponseTransaksi) {
        resp.dataTrans?.let { mAdapterTransaksi.addItem(it, clearAll = true) }
    }

    companion object {
        fun newInstance(
            reqToolbar: Boolean
        ) =
            TransaksiFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(REQ_TOOLBAR, reqToolbar)
                }
            }
    }
}