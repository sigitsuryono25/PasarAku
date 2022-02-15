package com.surelabsid.lti.pasaraku.ui.messages

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.base.Baseapp
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentMessageBinding
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.ui.messages.adapter.AdapterListMessages
import com.surelabsid.lti.pasaraku.utils.Constant
import kotlinx.coroutines.*


class MessageFragment : Fragment(R.layout.fragment_message) {
    private lateinit var binding: FragmentMessageBinding
    private lateinit var mAdapterListMessages: AdapterListMessages

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMessageBinding.bind(view)

        initAdapter()
        val incomingId = Prefs.getString(Constant.EMAIL)
        getRecentChat(incomingId)//user yang lagi login
    }

    private fun getRecentChat(incomingId: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO){
                try {
                    val res = NetworkModule.getService().getMessageList(incomingId = incomingId)
                    if(res.code == 200){
                        MainScope().launch {
                            initAdapter()
                            res.dataMessage?.let { mAdapterListMessages.addData(it) }
                        }
                    }
                }catch (e: Throwable){
                    e.printStackTrace()
                }
            }
        }
    }

    private fun initAdapter() {
        mAdapterListMessages = AdapterListMessages{
            Intent(requireActivity(), MessageActivity::class.java).apply {
                putExtra("email", it?.outgoingMsgId)
                putExtra("nama", it?.namaLengkap)
                startActivity(this)
            }
        }
        binding.rvListMessages.apply {
            adapter = mAdapterListMessages
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }
}