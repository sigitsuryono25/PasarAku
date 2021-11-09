package com.surelabsid.lti.pasaraku.ui.wilayah.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.surelabsid.lti.pasaraku.databinding.ItemAdapterWilayahBinding
import com.surelabsid.lti.pasaraku.response.DataProvinsiItem

class AdapterWilayah(private val onclick : (DataProvinsiItem?) -> Unit) : RecyclerView.Adapter<AdapterWilayah.ViewHolder>() {

    private val mListWilayah = mutableListOf<DataProvinsiItem?>()

    inner class ViewHolder(private val mItemAdapterWilayahBinding: ItemAdapterWilayahBinding) :
        RecyclerView.ViewHolder(mItemAdapterWilayahBinding.root) {
        fun onBindItem(dataProvinsiItem: DataProvinsiItem?){
            mItemAdapterWilayahBinding.root.setOnClickListener {
                onclick(dataProvinsiItem)
            }

            mItemAdapterWilayahBinding.namaWilayah.text = dataProvinsiItem?.nama
        }
    }

    fun addItem(item: List<DataProvinsiItem?>, clearIt: Boolean = false) {
        if (clearIt)
            mListWilayah.removeAll(mListWilayah)

        item.let {
            mListWilayah.addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdapterWilayahBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindItem(mListWilayah[position])
    }

    override fun getItemCount(): Int {
        return mListWilayah.size
    }


}