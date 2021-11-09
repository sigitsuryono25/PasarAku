package com.surelabsid.lti.pasaraku.ui.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.surelabsid.lti.pasaraku.databinding.ItemAdapterKategoriBinding
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.DataKategoriItem
import com.surelabsid.lti.pasaraku.utils.Constant

class AdapterKategori(private val clickItem: (DataKategoriItem?) -> Unit) :
    RecyclerView.Adapter<AdapterKategori.ViewHolder>() {

    private val mListKategori = mutableListOf<DataKategoriItem?>()

    inner class ViewHolder(val mItemAdapterKategoriBinding: ItemAdapterKategoriBinding) :
        RecyclerView.ViewHolder(mItemAdapterKategoriBinding.root) {

        fun onBindItem(dataKategoriItem: DataKategoriItem?) {
            mItemAdapterKategoriBinding.namaKategori.text = dataKategoriItem?.namaKategori
            Glide.with(mItemAdapterKategoriBinding.root.context)
                .load(NetworkModule.BASE_URL + Constant.URL_IMAGE + dataKategoriItem?.icon)
                .into(mItemAdapterKategoriBinding.icon)

            mItemAdapterKategoriBinding.root.setOnClickListener {
                clickItem(dataKategoriItem)
            }
        }
    }

    fun additem(item: List<DataKategoriItem?>, clearIt: Boolean = false) {
        if (clearIt)
            mListKategori.removeAll(mListKategori)
        item.let {
            mListKategori.addAll(item)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdapterKategoriBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindItem(mListKategori[position])
    }

    override fun getItemCount(): Int {
        return mListKategori.size
    }


}