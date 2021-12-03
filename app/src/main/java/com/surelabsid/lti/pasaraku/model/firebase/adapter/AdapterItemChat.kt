package com.surelabsid.lti.pasaraku.model.firebase.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.model.firebase.model.MessageItem
import com.surelabsid.lti.pasaraku.databinding.ItemAdapterChatBinding
import com.surelabsid.lti.pasaraku.utils.Constant
import com.surelabsid.lti.pasaraku.utils.TextHelper
import io.github.ponnamkarthik.richlinkpreview.MetaData
import io.github.ponnamkarthik.richlinkpreview.ResponseListener
import io.github.ponnamkarthik.richlinkpreview.RichPreview
import org.apache.commons.lang3.StringEscapeUtils

class AdapterItemChat(private val mListItem: List<MessageItem?>?) :
    RecyclerView.Adapter<AdapterItemChat.ViewHolder>() {

    class ViewHolder(itemView: ItemAdapterChatBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val containerMe = itemView.containerMe
        private val messageMe = itemView.messageMe
        private val jamMe = itemView.jamMe
        private val containerFrom = itemView.containerFrom
        private val messageFrom = itemView.messageFrom
        private val jamFrom = itemView.jamFrom
        private val rootThem = itemView.rootThem
        private val rootMe = itemView.rootMe
        private val titleMe = itemView.metaTitleMe
        private val metaMe = itemView.metaMe
        private val thumbnailMe = itemView.thumbnailMe
        private val linkMe = itemView.linkMe
        private val titleThem = itemView.metaTitleThem
        private val metaThem = itemView.metaThem
        private val linkThem = itemView.linkThem
        private val thumbnailThem = itemView.thumbnailThem

        fun onBind(messageItem: MessageItem?) {
            jamMe.text = messageItem?.added_on
            jamFrom.text = messageItem?.added_on
            if (messageItem?.user_id.toString() == Prefs.getString(Constant.EMAIL, null)) {
                containerFrom.visibility = View.GONE
                messageMe.text = StringEscapeUtils.unescapeJava(messageItem?.message)
                setLink(thumbnailMe, titleMe, metaMe, linkMe, rootMe, messageItem?.message)
            } else {
                containerMe.visibility = View.GONE
                messageFrom.text = StringEscapeUtils.unescapeJava(messageItem?.message)
                setLink(
                    thumbnailThem,
                    titleThem,
                    metaThem,
                    linkThem,
                    rootThem,
                    messageItem?.message
                )
            }

        }

        private fun setLink(
            thumbnail: ImageView,
            metaTitle: TextView,
            meta: TextView,
            link: TextView,
            root: RelativeLayout,
            message: String?
        ) {
            val richPreview = RichPreview(object : ResponseListener {
                override fun onData(metaData: MetaData) {

                    thumbnail.clipToOutline = true
                    if (metaData.imageurl != null) {
                        try {
                            Glide.with(itemView.context).load(metaData.imageurl).into(thumbnail)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    metaTitle.text = metaData.title
                    meta.text = metaData.description
                    link.text = metaData.url

                    root.visibility = View.VISIBLE
                }

                override fun onError(e: Exception) {
                    //handle error
                    e.printStackTrace()
                }
            })

            val s = TextHelper.pullLinks(message)
            s.map { links ->
                richPreview.getPreview(links)
                root.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(links)
                    startActivity(
                        itemView.context,
                        Intent.createChooser(intent, "Lanjutkan dengan..."), null
                    )
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAdapterChatBinding.inflate(
                LayoutInflater.from(parent.context)
                , parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(mListItem?.get(position))
    }

    override fun getItemCount(): Int {
        return mListItem?.size ?: 0
    }

}