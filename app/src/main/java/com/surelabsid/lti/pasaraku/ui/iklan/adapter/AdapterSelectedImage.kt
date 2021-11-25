package com.surelabsid.lti.pasaraku.ui.iklan.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.surelabsid.lti.pasaraku.databinding.ItemAdapterSelectedImageBinding


class AdapterSelectedImage : RecyclerView.Adapter<AdapterSelectedImage.ViewHolder>() {
    private val listImage = mutableListOf<Uri>()

    class ViewHolder(private val mItemAdapterSelectedImageBinding: ItemAdapterSelectedImageBinding) :
        RecyclerView.ViewHolder(mItemAdapterSelectedImageBinding.root) {
        fun onBindItem(path: Uri) {
            Glide.with(itemView.context)
                .load(path)
                .into(mItemAdapterSelectedImageBinding.imageItem)
        }
    }

    fun addItem(list: List<Uri>, clearIt: Boolean = false) {
        if (clearIt)
            this.listImage.removeAll(this.listImage)
        this.listImage.addAll(list)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdapterSelectedImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindItem(listImage[position])
    }

    override fun getItemCount(): Int {
        return listImage.size
    }

}