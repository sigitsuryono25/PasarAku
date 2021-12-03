package com.surelabsid.lti.pasaraku.model.firebase.messaging

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pixplicity.easyprefs.library.Prefs
import com.projectlauwba.lautku.firebase.messaging.AttachmentListDialogFragment
import com.surelabsid.lti.pasaraku.model.firebase.model.ChatHeader
import com.surelabsid.lti.pasaraku.model.firebase.model.FCMModel
import com.surelabsid.lti.pasaraku.model.firebase.model.MessageItem
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ActivityChatBinding
import com.surelabsid.lti.pasaraku.model.firebase.adapter.AdapterItemChat
import com.surelabsid.lti.pasaraku.response.ChatItem
import com.surelabsid.lti.pasaraku.utils.Constant
import org.apache.commons.lang3.StringEscapeUtils

class ChatActivity : AppCompatActivity() {

    private lateinit var vm: ChatViewModel
    private val mu = mutableListOf<MessageItem>()
    private lateinit var adapterItemChat: AdapterItemChat

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this)[ChatViewModel::class.java]
        val chatHeader = intent.getParcelableExtra<ChatHeader>(Constant.CHAT_HEADER)

        setSupportActionBar(binding.toolbarCustom)
        supportActionBar?.apply {
            title = chatHeader?.nama
        }

        binding.toolbarCustom.setNavigationOnClickListener {
            finish()
        }

        binding.sendBtn.setOnClickListener {
            if (binding.pesanContent.text?.trim().toString().isEmpty()) {
                Toast.makeText(
                    this@ChatActivity,
                    "Tidak dapat mengirimkan pesan kosong",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                sendMessage(
                    binding.pesanContent.text.toString(),
                    chatHeader
                )
            }
        }
        binding.attachment.setOnClickListener {
            val d = AttachmentListDialogFragment()
            d.show(supportFragmentManager, "attachment")
        }

        getItemChat(chatHeader)


    }

    private fun getItemChat(chatHeader: ChatHeader?) {
        vm.getItemChat(chatHeader)
        vm.itemChat.observe(this, {
            print(it.data?.get(0))
            setItemChat(it?.data)
        })

        vm.error.observe(this, {
            Log.d("getItemChat", "getItemChat: $it")
        })
    }

    private fun setItemChat(data: List<ChatItem?>?) {
        mu.removeAll(mu)
        data?.map {
            val msItem = MessageItem()
            msItem.user_id = it?.userId
            msItem.message = it?.message
            msItem.added_on = it?.addedOn
            mu.add(msItem)
        }
        binding.rvChat.adapter = null
        adapterItemChat = AdapterItemChat(mu)
        binding.rvChat.adapter = adapterItemChat
        val lm = LinearLayoutManager(this)
        lm.orientation = RecyclerView.VERTICAL
        binding.rvChat.layoutManager = lm
        scrollToBottom()
    }

    private fun sendMessage(message: String, chatHeader: ChatHeader?) {
        val m = MessageItem()
        m.message = StringEscapeUtils.escapeJava(message)
        m.user_id_dest = chatHeader?.user_id
        m.token_dest = chatHeader?.token
        m.user_id = Prefs.getString(Constant.EMAIL, null)
        m._id_chat = chatHeader?._id
        m.nama = chatHeader?.nama
        m.token_pengirim = Prefs.getString(Constant.TOKEN, null)

        chatHeader?.chatItem = m

        vm.insertChat(chatHeader)
        vm.res.observe(this, {
            Toast.makeText(this@ChatActivity, it.message, Toast.LENGTH_SHORT).show()
            pushNotification(m, chatHeader)
        })

        vm.error.observe(this, {
            Toast.makeText(this@ChatActivity, it, Toast.LENGTH_SHORT).show()
        })

        binding.pesanContent.text = null

    }

    private fun pushNotification(m: MessageItem, chatHeader: ChatHeader?) {
        vm.getItemChat(chatHeader)
        val mFCMModel = FCMModel()
        mFCMModel.token = m.token_dest
        mFCMModel.body = m

        vm.pushChat(mFCMModel)
        vm.responseBody.observe(this, {

        })
    }

    private fun scrollToBottom() {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isAcceptingText) {
            binding.rvChat.scrollToPosition(adapterItemChat.itemCount.minus(1))
        } else {
            binding.rvChat.scrollToPosition(adapterItemChat.itemCount.minus(1))
        }
    }
}