package com.surelabsid.lti.pasaraku.ui.wilayah.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.surelabsid.lti.pasaraku.databinding.ItemAdapterWilayahBinding
import com.surelabsid.lti.pasaraku.response.DataKabupatenItem
import com.surelabsid.lti.pasaraku.response.DataKecamatanItem
import com.surelabsid.lti.pasaraku.response.DataProvinsiItem

class AdapterWilayah(private val type: Int, private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<AdapterWilayah.ViewHolder>() {

    private val mListWilayah = mutableListOf<DataProvinsiItem?>()
    private val mListKabupaten = mutableListOf<DataKabupatenItem?>()
    private val mListKecamatan = mutableListOf<DataKecamatanItem?>()


    inner class ViewHolder(private val mItemAdapterWilayahBinding: ItemAdapterWilayahBinding) :
        RecyclerView.ViewHolder(mItemAdapterWilayahBinding.root) {
        fun onBindItem(dataProvinsiItem: DataProvinsiItem?) {
            mItemAdapterWilayahBinding.root.setOnClickListener {
                onItemClick.onProvSelected(dataProvinsiItem)
            }

            mItemAdapterWilayahBinding.namaWilayah.text = dataProvinsiItem?.nama?.trim()
        }

        fun onBindItemKabupaten(dataItemKabupatenItem: DataKabupatenItem?) {
            mItemAdapterWilayahBinding.namaWilayah.text = dataItemKabupatenItem?.nama?.trim()
            mItemAdapterWilayahBinding.root.setOnClickListener {
                onItemClick.onKabSelected(dataItemKabupatenItem)
            }
        }

        fun onBindItemKecamatan(dataKecamatanItem: DataKecamatanItem?) {
            mItemAdapterWilayahBinding.namaWilayah.text = dataKecamatanItem?.nama?.trim()
            mItemAdapterWilayahBinding.root.setOnClickListener {
                onItemClick.onKecSelected(dataKecamatanItem)
            }

        }


    }

    fun addItem(item: List<DataProvinsiItem?>, clearIt: Boolean = false) {
        if (clearIt)
            mListWilayah.removeAll(mListWilayah)

        mListWilayah.addAll(item)
        notifyDataSetChanged()
    }

    fun addItemKab(item: List<DataKabupatenItem?>, clearIt: Boolean = false) {
        if (clearIt)
            mListKabupaten.removeAll(mListKabupaten)

        mListKabupaten.addAll(item)
        notifyDataSetChanged()
    }

    fun addItemKec(item: List<DataKecamatanItem?>, clearIt: Boolean = false) {
        if (clearIt)
            mListKecamatan.removeAll(mListKecamatan)

        mListKecamatan.addAll(item)
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
        when (type) {
            PROVINSI_REQ -> holder.onBindItem(mListWilayah[position])
            KABUPATEN_REQ -> holder.onBindItemKabupaten(mListKabupaten[position])
            KECAMATAN_REQ -> holder.onBindItemKecamatan(mListKecamatan[position])
        }
    }

    override fun getItemCount(): Int {
        when (type) {
            PROVINSI_REQ -> return mListWilayah.size
            KABUPATEN_REQ -> return mListKabupaten.size
            KECAMATAN_REQ -> return mListKecamatan.size
        }
        return 0
    }


    interface OnItemClick {
        fun onKabSelected(dataItemKabupatenItem: DataKabupatenItem?) {}
        fun onKecSelected(dataKecamatanItem: DataKecamatanItem?) {}
        fun onProvSelected(dataItemProvinsiItem: DataProvinsiItem?) {}
    }

    companion object {
        const val KABUPATEN_REQ = 1045
        const val PROVINSI_REQ = 1055
        const val KECAMATAN_REQ = 1065
    }
}