package com.surelabsid.lti.pasaraku.model.firebase.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MessageItem(
    var message: String? = null,
    var user_id: String? = null,
    var user_id_dest: String? = null,
    var token_dest: String? = null,
    var nama: String? = null,
    var token_pengirim: String? = null,
    var added_on: String? = null,
    var _id_chat: Long? = ChatHeader()._id
) : Parcelable

@Parcelize
data class ChatHeader(
    var _id: Long? = null,
    var user_id: String? = null,
    var nama: String? = null,
    var chatItem: MessageItem? = null,
    var added_by: String? = null,
    var added_on: String? = null,
    var _id_ads: String? = null,
    var token: String? = null
) : Parcelable