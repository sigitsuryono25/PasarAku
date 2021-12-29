package com.surelabsid.lti.pasaraku.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ActivityChatFragmentBinding
import com.surelabsid.lti.pasaraku.model.firebase.messaging.ChatActivity
import com.surelabsid.lti.pasaraku.model.firebase.model.ChatHeader
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.ChatHeaderDataItem
import com.surelabsid.lti.pasaraku.ui.chat.adapter.AdapterChatHeader
import com.surelabsid.lti.pasaraku.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChatFragment : Fragment(R.layout.activity_chat_fragment) {
    private lateinit var vm: ChatHeaderViewModel
    private lateinit var binding: ActivityChatFragmentBinding
    private lateinit var adapterChatHeader : AdapterChatHeader
    private var kind: String? = "diterima"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(this).get(ChatHeaderViewModel::class.java)
        binding = ActivityChatFragmentBinding.bind(view)

        binding.refreshLayout.setOnRefreshListener {
            getChatHeader(kind)
            binding.refreshLayout.isRefreshing = false
        }

        vm.response.observe(viewLifecycleOwner, {
            setList(it.data)
        })
        vm.error.observe(this, {
            adapterChatHeader.addItemChatHeader(listOf(), clearAll = true)
        })



        adapterChatHeader = AdapterChatHeader({chatHeader ->
            startActivity(
                Intent(activity, ChatActivity::class.java).putExtra(
                    Constant.CHAT_HEADER,
                    chatHeader
                )
            )
        },{chatHeader, itemView ->
            val popup = PopupMenu(requireActivity(), itemView)
            //inflating menu from xml resource
            //inflating menu from xml resource
            popup.inflate(R.menu.options_menu)
            //adding click listener
            //adding click listener
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.hapusChat -> hapusChat(chatHeader)
                }
                return@setOnMenuItemClickListener false
            }
            //displaying the popup
            //displaying the popup
            popup.show()
        })

        binding.rvChatHeader.apply {
            adapter = adapterChatHeader
            layoutManager = LinearLayoutManager(requireActivity())
        }

        binding.kind.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                kind = binding.kind.selectedItem.toString()
                getChatHeader(kind)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }

    private fun getChatHeader(kind: String?) {
        vm.getChatList(Prefs.getString(Constant.EMAIL, null), kind)
    }

    private fun setList(data: List<ChatHeaderDataItem?>?) {
        val dataCh = mutableListOf<ChatHeader>()
        data?.forEach {
            val ch = ChatHeader()
            ch._id = it?.id?.toLong()
            ch.user_id = it?.userId
            ch.nama = it?.nama
            ch.added_by = it?.addedBy
            ch.token = it?.token
            ch.added_on = it?.addedOn
            dataCh.add(ch)
        }
        adapterChatHeader.addItemChatHeader(dataCh)
    }

    private fun hapusChat(chatHeader: ChatHeader?) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                val delete = NetworkModule.getService().deleteChat(chatHeader)
                if (delete.code == 200) {
                    withContext(Dispatchers.Main) {
                        getChatHeader(kind)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireActivity(), delete.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}