package com.surelabsid.lti.pasaraku.ui.messages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.surelabsid.lti.pasaraku.databinding.ItemAdapterListMessagesBinding
import com.surelabsid.lti.pasaraku.response.DataMessageItem

class AdapterListMessages(private val onClick: (DataMessageItem?) -> Unit) :
    RecyclerView.Adapter<AdapterListMessages.ViewHolder>() {
    inner class ViewHolder(val binding: ItemAdapterListMessagesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBindItem(dataMessageItem: DataMessageItem?) {
            binding.name.text = dataMessageItem?.namaLengkap
            binding.jam.text = dataMessageItem?.timestamp

            binding.root.setOnClickListener {
                onClick(dataMessageItem)
            }
        }

    }

    private var mListData = mutableListOf<DataMessageItem?>()

    fun addData(list: List<DataMessageItem?>, clearAll: Boolean = false) {
        if (clearAll)
            mListData = mutableListOf()

        mListData.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAdapterListMessagesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindItem(mListData.get(position))
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

}