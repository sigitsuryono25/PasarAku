package com.surelabsid.lti.pasaraku.ui.chat.adapter

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.surelabsid.lti.pasaraku.databinding.ItemAdapterChatHeaderBinding
import com.surelabsid.lti.pasaraku.model.firebase.model.ChatHeader

class AdapterChatHeader(
    private val onItemClick: (ChatHeader?) -> Unit,
    private val onLongClick: (ChatHeader?, itemView: View) -> Unit
) : RecyclerView.Adapter<AdapterChatHeader.ViewHolder>() {


    inner class ViewHolder(itemView: ItemAdapterChatHeaderBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val namaTujuan = itemView.namaTujuan
        private val jam = itemView.jam

        fun onBindItem(chatHeader: ChatHeader?) {
            namaTujuan.text = chatHeader?.nama
            itemView.setOnClickListener {
                onItemClick(chatHeader)
            }
            itemView.setOnLongClickListener {
                onLongClick(chatHeader, itemView)
                return@setOnLongClickListener true
            }
            jam.text = chatHeader?.added_on
        }

    }

    private val mListChatHeader = mutableListOf<ChatHeader?>()

    fun addItemChatHeader(list: List<ChatHeader?>, clearAll : Boolean = true){
        if(clearAll)
            mListChatHeader.removeAll(mListChatHeader)

        mListChatHeader.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAdapterChatHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindItem(mListChatHeader[position])
    }

    override fun getItemCount(): Int {
        return mListChatHeader.size
    }

}