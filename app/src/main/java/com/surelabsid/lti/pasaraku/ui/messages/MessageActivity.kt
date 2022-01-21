package com.surelabsid.lti.pasaraku.ui.messages

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.base.Baseapp
import com.surelabsid.lti.pasaraku.databinding.ActivityMessageBinding
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.ui.messages.adapter.AdapterMessages
import com.surelabsid.lti.pasaraku.utils.Constant
import kotlinx.coroutines.*

class MessageActivity : Baseapp() {
    private lateinit var binding: ActivityMessageBinding
    private lateinit var mAdapterMessages: AdapterMessages
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCustom)


        val uniqueId = intent.getStringExtra("email") //destinations
        val incomingId = Prefs.getString(Constant.EMAIL) //user yang lagi login
        val namaTujuan = intent.getStringExtra("nama") //user yang lagi login

        supportActionBar?.apply {
            title = namaTujuan
        }

        binding.toolbarCustom.setNavigationOnClickListener {
            finish()
        }

        mAdapterMessages = AdapterMessages()

        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = false
            getConversation(uniqueId, incomingId)
        }

        binding.sendBtn.setOnClickListener {
            if (binding.pesanContent.text.toString().isEmpty()) {
                Toast.makeText(
                    this@MessageActivity,
                    "Tidak dapat mengirimkan pesan kosong",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            sendMesg(uniqueId, incomingId, binding.pesanContent.text.toString())
        }


        getConversation(uniqueId, incomingId)
    }

    private fun sendMesg(uniqueId: String?, incomingId: String, mesg: String) {
        showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val res = NetworkModule.getService().insertChat(
                        uniqueId = uniqueId, incomingId = incomingId, message = mesg
                    )
                    if (res.code == 200) {
                        MainScope().launch {
                            dismissLoading()
                            binding.pesanContent.setText("")
                            getConversation(uniqueId, incomingId)
                        }
                    }
                } catch (e: Throwable) {
                    MainScope().launch {
                        dismissLoading()
                        Toast.makeText(this@MessageActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                    e.printStackTrace()
                }
            }
        }
    }

    private fun instanceAdapter() {
        binding.rvChat.apply {
            adapter = mAdapterMessages
            layoutManager = LinearLayoutManager(this@MessageActivity)
        }
    }

    private fun getConversation(uniqueId: String?, incomingId: String) {
        showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val data = NetworkModule.getService().getChat(uniqueId, incomingId)
                    MainScope().launch {
                        dismissLoading()
                        instanceAdapter()
                        data.chats?.let { mAdapterMessages.addChat(it, true) }
                        binding.rvChat.scrollToPosition(mAdapterMessages.listChat.size - 1)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    MainScope().launch {
                        dismissLoading()
                        Toast.makeText(this@MessageActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}