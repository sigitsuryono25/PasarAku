package com.surelabsid.lti.pasaraku.ui.akun.transaksi.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ItemAdapterTransaksiBinding
import com.surelabsid.lti.pasaraku.response.DataTransItem

class AdapterTransaksi(private val clickItem: (DataTransItem?) -> Unit) :
    RecyclerView.Adapter<AdapterTransaksi.ViewHolder>() {
    private val mListTrans = mutableListOf<DataTransItem?>()

    inner class ViewHolder(private val mItemAdapterTransaksiBinding: ItemAdapterTransaksiBinding) :
        RecyclerView.ViewHolder(mItemAdapterTransaksiBinding.root) {
        fun onBindItem(dataTransItem: DataTransItem?) {
            mItemAdapterTransaksiBinding.judulIklan.text = dataTransItem?.judulIklan
            mItemAdapterTransaksiBinding.nominal.text = "Rp. ${dataTransItem?.nominal}"
            mItemAdapterTransaksiBinding.status.text = dataTransItem?.statusPremium
            mItemAdapterTransaksiBinding.status.setBackgroundResource(R.drawable.bg_black)
            mItemAdapterTransaksiBinding.paket.text = "Paket: ${dataTransItem?.paket}"
            mItemAdapterTransaksiBinding.noInvoice.text = dataTransItem?.id
            if (dataTransItem?.isPremium == "Y") {
                mItemAdapterTransaksiBinding.status.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context, android.R.color.holo_green_dark)
                )
                mItemAdapterTransaksiBinding.tanggalPremium.visibility = View.VISIBLE
                mItemAdapterTransaksiBinding.start.text = "Mulai: ${dataTransItem?.startAt}"
                mItemAdapterTransaksiBinding.expired.text = "Berakhir: ${dataTransItem?.expired}"
            }

            if (dataTransItem?.isPremium == "N" && dataTransItem.alasanDitolak?.isNotEmpty() == true) {
                mItemAdapterTransaksiBinding.status.text = "Ditolak"
                mItemAdapterTransaksiBinding.alasanReject.text = dataTransItem.alasanDitolak
                mItemAdapterTransaksiBinding.alasanReject.visibility = View.VISIBLE
                mItemAdapterTransaksiBinding.status.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark)
                )
            }
            if (dataTransItem?.isPremium == "N" || dataTransItem?.isPremium == "W") {
                mItemAdapterTransaksiBinding.root.setOnClickListener {
                    clickItem(dataTransItem)
                }
            }
        }
    }

    fun addItem(listItem: List<DataTransItem?>, clearAll: Boolean = false) {
        if (clearAll)
            mListTrans.removeAll(mListTrans)

        mListTrans.addAll(listItem)
        notifyItemInserted(mListTrans.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAdapterTransaksiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindItem(mListTrans.get(position))
    }

    override fun getItemCount(): Int {
        return mListTrans.size
    }
}