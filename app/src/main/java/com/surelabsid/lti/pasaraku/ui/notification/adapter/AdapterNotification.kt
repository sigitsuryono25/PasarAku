package com.surelabsid.lti.pasaraku.ui.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.surelabsid.lti.pasaraku.database.Notifications
import com.surelabsid.lti.pasaraku.databinding.ItemAdapterNotificationBinding

class AdapterNotification : RecyclerView.Adapter<AdapterNotification.ViewHolder>() {

    private var mLisItem = mutableListOf<Notifications>()

    inner class ViewHolder(private val mItemAdapterNotificationBinding: ItemAdapterNotificationBinding) :
        RecyclerView.ViewHolder(mItemAdapterNotificationBinding.root) {

        fun onBindItem(notifications: Notifications) {
            mItemAdapterNotificationBinding.timestamp.text = notifications.timestamp
            mItemAdapterNotificationBinding.title.text = notifications.title
            mItemAdapterNotificationBinding.message.text = notifications.message
        }

    }

    fun addItem(list: List<Notifications>, clearAll: Boolean = false) {
        if (clearAll)
            mLisItem.removeAll(mLisItem)

        mLisItem.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAdapterNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindItem(mLisItem.get(position))
    }

    override fun getItemCount(): Int {
        return mLisItem.size
    }
}