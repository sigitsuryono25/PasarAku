package com.surelabsid.lti.pasaraku.ui.akun.transaksi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.surelabsid.lti.pasaraku.databinding.ItemAdapterTransaksiBinding
import com.surelabsid.lti.pasaraku.response.DataTransItem

class AdapterTransaksi(private val clickItem: (DataTransItem?) -> Unit) : RecyclerView.Adapter<AdapterTransaksi.ViewHolder>() {
    private val mListTrans = mutableListOf<DataTransItem?>()
    inner class ViewHolder(private val mItemAdapterTransaksiBinding: ItemAdapterTransaksiBinding) :
        RecyclerView.ViewHolder(mItemAdapterTransaksiBinding.root) {
        fun onBindItem(dataTransItem: DataTransItem?){
            mItemAdapterTransaksiBinding.judulIklan.text = dataTransItem?.judulIklan
            mItemAdapterTransaksiBinding.nominal.text = "Rp. ${dataTransItem?.nominal}"
            mItemAdapterTransaksiBinding.status.text = dataTransItem?.statusPremium
            mItemAdapterTransaksiBinding.paket.text = "Paket: ${dataTransItem?.paket}"
            mItemAdapterTransaksiBinding.noInvoice.text = dataTransItem?.id

            mItemAdapterTransaksiBinding.root.setOnClickListener {
                clickItem(dataTransItem)
            }
        }
    }

    fun addItem(listItem: List<DataTransItem?>, clearAll: Boolean = false){
        if(clearAll)
            mListTrans.removeAll(mListTrans)

        mListTrans.addAll(listItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(
            ItemAdapterTransaksiBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.onBindItem(mListTrans.get(position))
    }

    override fun getItemCount(): Int {
        return mListTrans.size
    }
}