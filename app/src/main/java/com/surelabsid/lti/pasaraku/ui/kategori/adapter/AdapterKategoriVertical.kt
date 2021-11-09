package com.surelabsid.lti.pasaraku.ui.kategori.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.surelabsid.lti.pasaraku.databinding.ItemAdapterKategoriVerticalBinding
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.DataKategoriItem
import com.surelabsid.lti.pasaraku.utils.Constant

class AdapterKategoriVertical(private val click: (DataKategoriItem?) -> Unit) :
    RecyclerView.Adapter<AdapterKategoriVertical.ViewHolder>() {

    private val mListKategori = mutableListOf<DataKategoriItem?>()

    inner class ViewHolder(private val mItemAdapterKategoriVerticalBinding: ItemAdapterKategoriVerticalBinding) :
        RecyclerView.ViewHolder(mItemAdapterKategoriVerticalBinding.root) {

        fun onBindItem(dataKategoriItem: DataKategoriItem?) {
            mItemAdapterKategoriVerticalBinding.namaKategori.text = dataKategoriItem?.namaKategori
            Glide.with(itemView.context)
                .load(NetworkModule.BASE_URL + Constant.URL_IMAGE + dataKategoriItem?.icon)
                .into(mItemAdapterKategoriVerticalBinding.icon)

            itemView.setOnClickListener {
                click(dataKategoriItem)
            }
        }
    }

    fun addItem(item: List<DataKategoriItem?>, clearIt: Boolean = false) {
        if (clearIt)
            mListKategori.removeAll(mListKategori)

        item.let {
            mListKategori.addAll(it)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdapterKategoriVerticalBinding.inflate(
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