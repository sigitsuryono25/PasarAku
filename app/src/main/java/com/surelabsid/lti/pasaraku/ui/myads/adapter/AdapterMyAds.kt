package com.surelabsid.lti.pasaraku.ui.myads.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ItemMyAdsBinding
import com.surelabsid.lti.pasaraku.response.DataIklanItem
import com.surelabsid.lti.pasaraku.ui.iklan.TambahIklanActivity
import com.surelabsid.lti.pasaraku.utils.Constant


class AdapterMyAds(
    private val onRootClick: (DataIklanItem?) -> Unit,
    private val onPremiumClick: (DataIklanItem?) -> Unit,
    private val hapusIklan: (DataIklanItem?) -> Unit,
    private val editIklan: (DataIklanItem?) -> Unit
) : RecyclerView.Adapter<AdapterMyAds.ViewHolder>() {

    private val listIklan = mutableListOf<DataIklanItem?>()

    inner class ViewHolder(private val mItemMyAdsBinding: ItemMyAdsBinding) :
        RecyclerView.ViewHolder(mItemMyAdsBinding.root) {

        fun onBindItem(dataIklanItem: DataIklanItem?) {
            mItemMyAdsBinding.harga.text = dataIklanItem?.harga
            mItemMyAdsBinding.judulIklan.text = dataIklanItem?.judulIklan
            if (dataIklanItem?.foto?.isNotEmpty() == true) {
                val cover = dataIklanItem.foto.first()
                val urlGambar = Constant.ADS_PIC_URL + dataIklanItem.iklanId + "/" + cover
                Glide.with(itemView.context)
                    .load(urlGambar)
                    .into(mItemMyAdsBinding.coverImage)
            }

            if (dataIklanItem?.isPremium == "Y") {
                mItemMyAdsBinding.premium.visibility = View.GONE
            }

            mItemMyAdsBinding.premium.setOnClickListener {
                onPremiumClick(dataIklanItem)
            }

            mItemMyAdsBinding.root.setOnClickListener {
                onRootClick(dataIklanItem)
            }

            mItemMyAdsBinding.more.setOnClickListener {
                initiatePopupMenu(dataIklanItem)
            }
        }


        private fun initiatePopupMenu(dataIklanItem: DataIklanItem?) {
            val popup = PopupMenu(itemView.context, mItemMyAdsBinding.more)
            popup.inflate(R.menu.options_menu_my_ads)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.hapusAds -> {
                        hapusIklan(dataIklanItem)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.editAds -> {
                        editIklan(dataIklanItem)
                        return@setOnMenuItemClickListener true
                    }

                }
                return@setOnMenuItemClickListener false
            }
            popup.show()
        }

    }

    fun addItem(list: List<DataIklanItem?>?, clearAll: Boolean = false) {
        if (clearAll)
            listIklan.removeAll(listIklan)

        list?.let { listIklan.addAll(it) }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMyAdsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindItem(listIklan[position])
    }

    override fun getItemCount(): Int {
        return listIklan.size
    }
}