package com.surelabsid.lti.pasaraku.ui.explore.adapter

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ItemAdapterIklanBinding
import com.surelabsid.lti.pasaraku.databinding.ItemAdapterIklanVerticalBinding
import com.surelabsid.lti.pasaraku.response.DataIklanItem
import com.surelabsid.lti.pasaraku.utils.Constant


class AdapterIklan(
    private val isGrid: Boolean = true,
    private val onClick: (DataIklanItem?) -> Unit,
    private val onFavClick: (DataIklanItem?, ImageView) -> Unit,
    private val width: Int = LinearLayout.LayoutParams.MATCH_PARENT
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListIklan = mutableListOf<DataIklanItem?>()

    inner class ViewHolderGrid(val mItemAdapterIklanBinding: ItemAdapterIklanBinding) :
        RecyclerView.ViewHolder(mItemAdapterIklanBinding.root) {
        fun onBindItem(dataIklanItem: DataIklanItem?) {
            if (dataIklanItem?.foto?.isNotEmpty() == true) {
                val cover = dataIklanItem.foto.first()
                val urlGambar = Constant.ADS_PIC_URL + dataIklanItem.iklanId + "/" + cover
                Glide.with(itemView.context)
                    .load(urlGambar)
                    .into(mItemAdapterIklanBinding.coverImage)
                if (width != LinearLayout.LayoutParams.MATCH_PARENT) {
                    val lparams = LinearLayout.LayoutParams(
                        width,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    lparams.setMargins(10,10,10,10)
                    mItemAdapterIklanBinding.root.layoutParams = lparams
                }

            }

            itemView.setOnClickListener {
                onClick(dataIklanItem)
            }
            mItemAdapterIklanBinding.favorite.setOnClickListener {
                onFavClick(dataIklanItem, mItemAdapterIklanBinding.favorite)
            }

            if (dataIklanItem?.fav == true) {
                Glide.with(itemView.context)
                    .load(R.drawable.ic_baseline_favorite)
                    .into(mItemAdapterIklanBinding.favorite)
            }

            if (dataIklanItem?.isPremium.equals("Y", true)) {
                mItemAdapterIklanBinding.isPremium.visibility = View.VISIBLE
            }

            mItemAdapterIklanBinding.judulIklan.text = dataIklanItem?.judulIklan
            mItemAdapterIklanBinding.harga.text = dataIklanItem?.harga

            val location = Location(itemView.context.getString(R.string.app_name))
            location.latitude = dataIklanItem?.lat.toString().toDouble()
            location.longitude = dataIklanItem?.lon.toString().toDouble()

//            val listAddress = GPSTracker(itemView.context).geocoder(location)
//            if (listAddress.isNotEmpty()) {
//                val kab = listAddress.iterator().next().subAdminArea
//                val kec = listAddress.iterator().next().locality
//                val prov = listAddress.iterator().next().adminArea
//                mItemAdapterIklanBinding.lokasi.text = "$kec, $kab, $prov"
//            } else {
//                mItemAdapterIklanBinding.lokasi.text = "Lokasi tidak diketahui"
            if (dataIklanItem?.idProv?.isEmpty() == true && dataIklanItem.idKab?.isEmpty() == true && dataIklanItem.idKec?.isEmpty() == true) {
                mItemAdapterIklanBinding.lokasi.text = "Indonesia"
            } else {
                var lokasi = ""
                if (dataIklanItem?.kec?.isNotEmpty() == true) {
                    lokasi += "${dataIklanItem.kec}, "
                }
                if (dataIklanItem?.kab?.isNotEmpty() == true) {
                    lokasi += "${dataIklanItem.kab}, "
                }
                if (dataIklanItem?.prov?.isNotEmpty() == true) {
                    lokasi += "${dataIklanItem.prov}"
                }
                mItemAdapterIklanBinding.lokasi.text =
                    lokasi.trim()

            }
//            }
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

//            val listAddress = GPSTracker(itemView.context).geocoder(location)
//            if (listAddress.isNotEmpty()) {
//                val kab = listAddress.iterator().next().subAdminArea
//                val kec = listAddress.iterator().next().locality
//                val prov = listAddress.iterator().next().adminArea
//
            if (dataIklanItem?.prov?.isEmpty() == true && dataIklanItem.kab?.isEmpty() == true && dataIklanItem.kec?.isEmpty() == true) {
                mItemAdapterIklanVerticalBinding.lokasi.text = "Indonesia"
            } else {
                var lokasi = ""
                if (dataIklanItem?.kec?.isNotEmpty() == true) {
                    lokasi += "${dataIklanItem.kec}, "
                }
                if (dataIklanItem?.kab?.isNotEmpty() == true) {
                    lokasi += "${dataIklanItem.kab}, "
                }
                if (dataIklanItem?.prov?.isNotEmpty() == true) {
                    lokasi += "${dataIklanItem.prov}"
                }
                mItemAdapterIklanVerticalBinding.lokasi.text =
                    lokasi.trim()

            }
//            } else {
//                mItemAdapterIklanVerticalBinding.lokasi.text = "Lokasi tidak diketahui"
//            }
        }
    }


    fun removeAllItems() {
        mListIklan = mutableListOf()
        notifyDataSetChanged()
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