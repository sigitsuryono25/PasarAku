package com.surelabsid.lti.pasaraku.ui.explore.adapter

import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ItemAdapterIklanBinding
import com.surelabsid.lti.pasaraku.databinding.ItemAdapterIklanVerticalBinding
import com.surelabsid.lti.pasaraku.response.DataIklanItem
import com.surelabsid.lti.pasaraku.utils.Constant
import com.surelabsid.lti.pasaraku.utils.GPSTracker


class AdapterIklan(
    private val isGrid: Boolean = true,
    private val onClick: (DataIklanItem?) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mListIklan = mutableListOf<DataIklanItem?>()

    inner class ViewHolderGrid(val mItemAdapterIklanBinding: ItemAdapterIklanBinding) :
        RecyclerView.ViewHolder(mItemAdapterIklanBinding.root) {
        fun onBindItem(dataIklanItem: DataIklanItem?) {
            if (dataIklanItem?.foto?.isNotEmpty() == true) {
                val cover = dataIklanItem.foto.first()
                val urlGambar = Constant.ADS_PIC_URL + dataIklanItem.iklanId + "/" + cover
                Glide.with(itemView.context)
                    .load(urlGambar)
                    .into(mItemAdapterIklanBinding.coverImage)
            }

            itemView.setOnClickListener {
                onClick(dataIklanItem)
            }
            mItemAdapterIklanBinding.judulIklan.text = dataIklanItem?.judulIklan
            mItemAdapterIklanBinding.harga.text = dataIklanItem?.harga

            val location = Location(itemView.context.getString(R.string.app_name))
            location.latitude = dataIklanItem?.lat.toString().toDouble()
            location.longitude = dataIklanItem?.lon.toString().toDouble()

            val listAddress = GPSTracker(itemView.context).geocoder(location)
            if (listAddress.isNotEmpty()) {
                val kab = listAddress.iterator().next().subAdminArea
                val kec = listAddress.iterator().next().locality
                val prov = listAddress.iterator().next().adminArea
                mItemAdapterIklanBinding.lokasi.text = "$kec, $kab, $prov"
            }else{
                mItemAdapterIklanBinding.lokasi.text = "Lokasi tidak diketahui"
            }
        }
    }

    inner class ViewHolderVertical(val mItemAdapterIklanVerticalBinding: ItemAdapterIklanVerticalBinding) :
        RecyclerView.ViewHolder(mItemAdapterIklanVerticalBinding.root) {
        fun onBindItem(dataIklanItem: DataIklanItem?) {
            if (dataIklanItem?.foto?.isNotEmpty() == true) {
                val cover = dataIklanItem.foto.first()
                val urlGambar = Constant.ADS_PIC_URL + dataIklanItem.iklanId + "/" + cover
                Glide.with(itemView.context)
                    .load(urlGambar)
                    .into(mItemAdapterIklanVerticalBinding.coverImage)
            }

            itemView.setOnClickListener {
                onClick(dataIklanItem)
            }
            mItemAdapterIklanVerticalBinding.judulIklan.text = dataIklanItem?.judulIklan
            mItemAdapterIklanVerticalBinding.harga.text = dataIklanItem?.harga

            val location = Location(itemView.context.getString(R.string.app_name))
            location.latitude = dataIklanItem?.lat.toString().toDouble()
            location.longitude = dataIklanItem?.lon.toString().toDouble()

            val listAddress = GPSTracker(itemView.context).geocoder(location)
            if(listAddress.isNotEmpty()) {
                val kab = listAddress.iterator().next().subAdminArea
                val kec = listAddress.iterator().next().locality
                val prov = listAddress.iterator().next().adminArea

                mItemAdapterIklanVerticalBinding.lokasi.text = "$kec, $kab, $prov"
            }else{
                mItemAdapterIklanVerticalBinding.lokasi.text = "Lokasi tidak diketahui"
            }
        }
    }

    fun addIklan(adsList: List<DataIklanItem?>, clearIt: Boolean = false) {
        if (clearIt)
            mListIklan.removeAll(mListIklan)

        mListIklan.addAll(adsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (isGrid) {
            return ViewHolderGrid(
                ItemAdapterIklanBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return ViewHolderVertical(
                ItemAdapterIklanVerticalBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isGrid) {
            val grid = holder as ViewHolderGrid
            grid.onBindItem(mListIklan[position])
        } else {
            val vertical = holder as ViewHolderVertical
            vertical.onBindItem(mListIklan[position])
        }
    }

    override fun getItemCount(): Int {
        return mListIklan.size
    }

}