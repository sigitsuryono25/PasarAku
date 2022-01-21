package com.surelabsid.lti.pasaraku.ui.messages.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.surelabsid.lti.pasaraku.databinding.ItemAdapterMessagesBinding
import com.surelabsid.lti.pasaraku.response.ChatsItem

class AdapterMessages: RecyclerView.Adapter<AdapterMessages.ViewHolder>() {
    inner class ViewHolder(val binding: ItemAdapterMessagesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBindItem(chats: ChatsItem?) {
            if (chats?.outgoing == true) {
                binding.me.text = chats.msg
                binding.containerMe.visibility = View.VISIBLE
            } else {
                binding.coming.text = chats?.msg
                binding.containerComing.visibility = View.VISIBLE
            }
        }

    }

    var listChat = mutableListOf<ChatsItem?>()

    fun addChat(list: List<ChatsItem?>, clearAll: Boolean = false) {
        if (clearAll)
            listChat = mutableListOf()

        listChat.addAll(list)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAdapterMessagesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindItem(listChat.get(position))
    }

    override fun getItemCount(): Int {
        return listChat.size
    }

}